package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Respository.DocumentDAO;
import com.hunghq.librarymanagement.Service.CallAPIService;
import com.hunghq.librarymanagement.Service.LoadImageService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainAllBooksController extends BaseController {
    @FXML
    public GridPane gp_all_books;
    @FXML
    public Button btn_previous;
    @FXML
    public Label lbl_page_info;
    @FXML
    public Button btn_next;

    private ObservableList<Document> allBooks;

    private int offset = 0;
    private int currentPage = 1;
    private final int limit = 15;
    private int totalPages;
    private DocumentDAO documentDAO = new DocumentDAO();

    private CallAPIService callAPIService;
    private LoadImageService loadImageService;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        callAPIService = new CallAPIService();
        loadImageService = new LoadImageService(callAPIService);

        loadBooks();

        btn_previous.setOnAction(event -> {
            if (currentPage > 1) {
                currentPage--;
                offset -= limit;
                loadBooks();
            }
        });

        btn_next.setOnAction(event -> {
            if (!allBooks.isEmpty()) {
                currentPage++;
                offset += limit;
                loadBooks();
            }
        });
    }

    private void loadBooks() {
        allBooks = documentDAO.getAllWithOffset(offset);
        gp_all_books.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Document document : allBooks) {
            VBox vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefHeight(150);
            vBox.setMaxHeight(150);

            ImageView imageView = new ImageView();
            imageView.setFitHeight(120);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            vBox.getChildren().add(imageView);

            Label nameLabel = new Label(document.getTitle());
            nameLabel.setWrapText(true);
            nameLabel.setMaxWidth(120);
            nameLabel.setStyle("-fx-alignment: center;");
            vBox.getChildren().add(nameLabel);

            loadImageService.loadImage(document, imageView);

            gp_all_books.add(vBox, column, row);
            vBox.getStyleClass().add("service-box");
            imageView.getStyleClass().add("image-box");

            vBox.setOnMouseClicked(event -> {
                showBookDetail(document);
            });

            vBox.setOnMouseEntered(event -> {
                vBox.setOpacity(0.8);
                vBox.setStyle("-fx-cursor: hand;");
            });

            vBox.setOnMouseExited(event -> {
                vBox.setOpacity(1.0);
            });

            column++;
            if (column == 5) {
                column = 0;
                row++;
            }
        }

        lbl_page_info.setText("Page " + currentPage);
        btn_previous.setDisable(currentPage == 1);
        btn_next.setDisable(allBooks.size() < limit);
    }

    public void updateBooks(ObservableList<Document> searchResults) {
        totalPages = (int) Math.ceil((double) searchResults.size() / limit);

        displayPage(searchResults);

        btn_previous.setDisable(currentPage == 1);
        btn_next.setDisable(currentPage == totalPages);
    }

    private void displayPage(ObservableList<Document> books) {
        gp_all_books.getChildren().clear();

        int startIndex = (currentPage - 1) * limit;
        int endIndex = Math.min(startIndex + limit, books.size());

        int column = 0;
        int row = 0;

        for (int i = startIndex; i < endIndex; i++) {
            Document document = books.get(i);

            VBox vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefHeight(150);
            vBox.setMaxHeight(150);

            ImageView imageView = new ImageView();
            imageView.setFitHeight(120);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            vBox.getChildren().add(imageView);

            Label nameLabel = new Label(document.getTitle());
            nameLabel.setWrapText(true);
            nameLabel.setMaxWidth(120);
            nameLabel.setStyle("-fx-alignment: center;");
            vBox.getChildren().add(nameLabel);

            loadImageService.loadImage(document, imageView);

            gp_all_books.add(vBox, column, row);
            vBox.getStyleClass().add("service-box");
            imageView.getStyleClass().add("image-box");

            vBox.setOnMouseClicked(event -> {
                showBookDetail(document);
            });

            vBox.setOnMouseEntered(event -> {
                vBox.setOpacity(0.8);
                vBox.setStyle("-fx-cursor: hand;");
            });

            vBox.setOnMouseExited(event -> {
                vBox.setOpacity(1.0);
            });

            column++;
            if (column == 5) {
                column = 0;
                row++;
            }
        }

        lbl_page_info.setText("Page " + currentPage + " of " + totalPages);
    }

    private void showBookDetail(Document document) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hunghq/librarymanagement/View/AllBooks/BookDetail.fxml"));

            Scene scene = new Scene(fxmlLoader.load());

            BookDetailController bookDetailController = fxmlLoader.getController();

            bookDetailController.initialize(document);

            Stage stage = new Stage();
            stage.setTitle("Book Detail");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


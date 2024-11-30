package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Global.AppProperties;
import com.hunghq.librarymanagement.Model.Entity.BorrowDocument;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Model.Enum.EState;
import com.hunghq.librarymanagement.Respository.BorrowDocumentDAO;
import com.hunghq.librarymanagement.Service.CallAPIService;
import com.hunghq.librarymanagement.Service.LoadImageService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class MainMyBooksController extends BaseController {
    @FXML
    public GridPane gp_all_books;
    @FXML
    public Button btn_previous;
    @FXML
    public Label lbl_page_info;
    @FXML
    public Button btn_next;
    @FXML
    public ComboBox<String> cb_filter_state;

    private ObservableList<BorrowDocument> allBorrowBooks;
    private ObservableList<BorrowDocument> filteredList;


    private int currentPage = 1;
    private final int limit = 10;
    private int totalPages;
    private BorrowDocumentDAO borrowDocumentDAO = new BorrowDocumentDAO();

    private CallAPIService callAPIService;
    private LoadImageService loadImageService;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        callAPIService = new CallAPIService();
        loadImageService = new LoadImageService(callAPIService);

        cb_filter_state.getItems().addAll("All", "Borrowed", "Overdue");
        cb_filter_state.setValue("All");

        cb_filter_state.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterBorrowDocumentByState(newValue);
        });

        loadBooks();

        btn_previous.setOnAction(event -> {
            if (currentPage > 1) {
                currentPage--;
                displayPage(filteredList);
                updatePaginationButtons();
            }
        });

        btn_next.setOnAction(event -> {
            if (!filteredList.isEmpty() && currentPage < totalPages) {
                currentPage++;
                displayPage(filteredList);
                updatePaginationButtons();
            }
        });

    }

    private void filterBorrowDocumentByState(String filterState) {
        switch (filterState) {
            case "Borrowed":
                filteredList = allBorrowBooks.filtered(borrowDocument -> borrowDocument.getState().equals(EState.fromValue("Borrowed")));
                break;
            case "Overdue":
                filteredList = allBorrowBooks.filtered(borrowDocument -> borrowDocument.getState().equals(EState.fromValue("Overdue")));
                break;
                default:
                    filteredList = allBorrowBooks;
                    break;
        }

        currentPage = 1;
        totalPages = (int) Math.ceil((double) filteredList.size() / limit);
        displayPage(filteredList);
        updatePaginationButtons();
    }

    private void loadBooks() {
        allBorrowBooks = borrowDocumentDAO.getBorrowDocumentByUserId(parseInt(AppProperties.getProperty("user.userId")));
        filterBorrowDocumentByState(cb_filter_state.getValue());

        totalPages = (int) Math.ceil((double) allBorrowBooks.size() / limit);

        filterBorrowDocumentByState(cb_filter_state.getValue());
        displayPage(allBorrowBooks);
        updatePaginationButtons();
    }

    public void updateBooks(ObservableList<BorrowDocument> searchResults) {
        allBorrowBooks = searchResults;
        totalPages = (int) Math.ceil((double) searchResults.size() / limit);
        currentPage = 1;
        displayPage(allBorrowBooks);
        updatePaginationButtons();
    }

    private void displayPage(ObservableList<BorrowDocument> books) {
        gp_all_books.getChildren().clear();

        int startIndex = (currentPage - 1) * limit;
        int endIndex = Math.min(startIndex + limit, books.size());

        int column = 0;
        int row = 0;

        for (int i = startIndex; i < endIndex; i++) {
            BorrowDocument borrowDocument = books.get(i);

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

            Label nameLabel = new Label(borrowDocument.getDocument().getTitle());
            nameLabel.setWrapText(true);
            nameLabel.setMaxWidth(120);
            nameLabel.setStyle("-fx-alignment: center;");
            vBox.getChildren().add(nameLabel);

            loadImageService.loadImageWithEntity(borrowDocument.getDocument(), imageView);

            gp_all_books.add(vBox, column, row);
            vBox.getStyleClass().add("service-box");
            imageView.getStyleClass().add("image-box");

            vBox.setOnMouseClicked(event -> {
                showBookDetail(borrowDocument.getDocument());
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
            scene.getStylesheets().add(getClass().getResource("/com/hunghq/librarymanagement/Style/style.css").toExternalForm());

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

    private void updatePaginationButtons() {
        btn_previous.setDisable(currentPage == 1);
        btn_next.setDisable(currentPage == totalPages || allBorrowBooks.isEmpty());
    }
}


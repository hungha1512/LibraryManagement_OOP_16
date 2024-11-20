package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Respository.DocumentDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainAllBooksController extends BaseController {
    @FXML
    public GridPane gp_all_books;

    private ObservableList<Document> allBooks;

    private int offset = 0;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DocumentDAO documentDAO = new DocumentDAO();
        allBooks = documentDAO.getAllWithOffset(offset);

        int column = 0;
        int row = 0;

        for (Document document : allBooks) {
            String coverImg = document.getCoverImg();
            Image image = null;
            // TODO: Fixing the image file with real URL, now is error with IllegalException
            if (coverImg != null) {
                image = new Image(coverImg);
            } else {
                String defaultImage = "src/main/resources/com/hunghq/librarymanagement/Media/PT.jpg";
                File file = new File(defaultImage);
                image = new Image(file.toURI().toString());
            }
            ImageView imageView = new ImageView(image);

            imageView.setFitHeight(120);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            Label nameLabel = new Label(document.getTitle());
            nameLabel.setWrapText(true);
            nameLabel.setMaxWidth(120);
            nameLabel.setStyle("-fx-alignment: center;");

            VBox vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefHeight(150);
            vBox.setMaxHeight(150);
            vBox.getChildren().addAll(imageView, nameLabel);

            gp_all_books.add(vBox, column, row);
            vBox.getStyleClass().add("service-box");
            imageView.getStyleClass().add("image-box");

            column++;
            if (column == 5) {
                column = 0;
                row++;
            }

            // TODO: Do pagination, with each page has 15 books
        }
    }
}

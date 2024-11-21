package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Service.CallAPIService;
import com.hunghq.librarymanagement.Service.LoadImageService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookDetailController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField languageField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField publishedDateField;
    @FXML
    private TextField awardField;
    @FXML
    private TextField idField;
    @FXML
    private ImageView coverImageView;

    private CallAPIService callAPIService;
    private LoadImageService loadImageService;

    public void initialize(Document document) {

        callAPIService = new CallAPIService();
        loadImageService = new LoadImageService(callAPIService);

        if (document != null) {

            titleField.setText(document.getTitle());
            authorField.setText(document.getAuthorName());
            languageField.setText(document.getLanguage());
            genreField.setText(document.getGenre());
            publisherField.setText(document.getPublisher());
            isbnField.setText(document.getIsbn());
            publishedDateField.setText(document.getPublishedDate());
            awardField.setText(document.getAward());
            idField.setText(document.getDocumentId());

            loadImageService.loadImage(document, coverImageView);
        }
    }

}

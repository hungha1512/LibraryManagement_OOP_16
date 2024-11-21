package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Service.CallAPIService;
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

    private CallAPIService apiService = new CallAPIService();

    public void initialize(Document document) {
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

            loadImageAsync(document);
        }
    }

    private void loadImageAsync(Document document) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() {
                String imageUrl = apiService.getImageUrlFromTitle(document.getTitle());
                if (imageUrl != null) {
                    try {
                        return new Image(imageUrl, true);
                    } catch (Exception e) {
                        System.err.println("Error loading image: " + e.getMessage());
                    }
                }
                return new Image("/src/main/resources/com/hunghq/librarymanagement/Media/LogoUet.jpg");
            }
        };

        loadImageTask.setOnSucceeded(event -> coverImageView.setImage(loadImageTask.getValue()));

        loadImageTask.setOnFailed(event -> {
            System.err.println("Failed to load image for: " + document.getTitle());
            coverImageView.setImage(new Image("/src/main/resources/com/hunghq/librarymanagement/Media/LogoUet.jpg"));
        });
        new Thread(loadImageTask).start();
    }


}

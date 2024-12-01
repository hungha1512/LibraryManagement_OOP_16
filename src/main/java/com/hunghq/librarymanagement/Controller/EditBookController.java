package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Global.GenerateDocumentID;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Respository.DocumentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditBookController {
    @FXML
    private TextField tf_document_id;
    @FXML
    private TextField tf_title;
    @FXML
    private TextField tf_author;
    @FXML
    private TextField tf_rating;
    @FXML
    private TextArea ta_description;
    @FXML
    private TextField tf_language;
    @FXML
    private TextField tf_isbn;
    @FXML
    private TextArea ta_genre;
    @FXML
    private TextField tf_quantity;
    @FXML
    private TextField tf_publisher;
    @FXML
    private TextField tf_published_date;
    @FXML
    private TextArea ta_award;
    @FXML
    private TextField tf_num_ratings;
    @FXML
    private TextField tf_cover_image;

    @FXML
    private Button btn_update;
    @FXML
    private Button btn_delete_all;

    private Document document;
    private DocumentDAO documentDAO;

    private MainManageController mainManageController;

    public void initialize(Document document) {
        documentDAO = new DocumentDAO();
        this.document = document;

        setContentToTextField();

        btn_update.setOnAction(event -> {handleUpdate();});
        btn_delete_all.setOnAction(event -> {handleDeleteAll();});
    }

    private void setContentToTextField() {
        tf_author.setText(document.getAuthor());
        tf_title.setText(document.getTitle());
        ta_description.setText(document.getDescription());
        tf_language.setText(document.getLanguage());
        tf_isbn.setText(document.getIsbn());
        ta_genre.setText(document.getGenre());

        tf_document_id.setText(document.getDocumentId());
        tf_document_id.setEditable(false);

        tf_publisher.setText(document.getPublisher());
        tf_published_date.setText(document.getPublishedDate());
        ta_award.setText(document.getAward());
        tf_quantity.setText(String.valueOf(document.getQuantity()));
        tf_num_ratings.setText(String.valueOf(document.getNumRatings()));
        tf_cover_image.setText(document.getCoverImg());
        tf_rating.setText(String.valueOf(document.getRating()));
    }

    private void handleDeleteAll() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to clear all fields?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                tf_author.clear();
                tf_title.clear();
                ta_description.clear();
                tf_language.clear();
                tf_isbn.clear();
                ta_genre.clear();
                tf_publisher.clear();
                tf_published_date.clear();
                ta_award.clear();
                tf_quantity.clear();
                tf_num_ratings.clear();
                tf_cover_image.clear();
                tf_rating.clear();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Fields Cleared");
                successAlert.setHeaderText(null);
                successAlert.setContentText("All fields have been cleared successfully.");
                successAlert.showAndWait();
            } else {
                Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelAlert.setTitle("Action Cancelled");
                cancelAlert.setHeaderText(null);
                cancelAlert.setContentText("No fields were cleared.");
                cancelAlert.showAndWait();
            }
        });
    }


    private void handleUpdate() {
        this.document = new Document(
                document.getDocumentId(),
                tf_title.getText(),
                tf_author.getText(),
                Double.parseDouble(tf_rating.getText()),
                ta_description.getText(),
                tf_language.getText(),
                tf_isbn.getText(),
                ta_genre.getText(),
                Integer.parseInt(tf_quantity.getText()),
                tf_publisher.getText(),
                tf_published_date.getText(),
                ta_award.getText(),
                Integer.parseInt(tf_num_ratings.getText()),
                tf_cover_image.getText()
        );

        boolean isUpdated = documentDAO.update(this.document);

        Alert alert = new Alert(isUpdated ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(isUpdated ? "Update Successful" : "Update Failed");
        alert.setHeaderText(null);
        alert.setContentText(isUpdated ? "The document was updated successfully." : "Failed to update the document. Please try again.");
        alert.showAndWait();
    }


}

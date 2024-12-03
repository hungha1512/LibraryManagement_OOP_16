package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Global.GenerateDocumentID;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Respository.DocumentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

import static com.hunghq.librarymanagement.Global.Format.formatInput;

public class AddBookController {
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
    private Button btn_add;
    @FXML
    private Button btn_delete_all;

    private Document document;
    private DocumentDAO documentDAO;

    private MainManageController mainManageController;
    private GenerateDocumentID genID;
    private String documentID;

    public void setMainManageController(MainManageController mainManageController) {
        this.mainManageController = mainManageController;
    }

    @FXML
    public void initialize() {
        genID = new GenerateDocumentID();
        documentDAO = new DocumentDAO();
        documentID = genID.generateDocumentID();
        initializePromptText();

        btn_add.setOnAction(event -> {handleAdd();});
        btn_delete_all.setOnAction(event -> {handleDeleteAll();});

        tf_document_id.setText(documentID);
        tf_document_id.setEditable(false);
    }

    private void initializePromptText() {
        tf_title.setPromptText("Enter book title");
        tf_author.setPromptText("Enter author name");
        tf_rating.setPromptText("Enter book rating");
        ta_description.setPromptText("Enter book description");
        tf_language.setPromptText("Enter language");
        tf_isbn.setPromptText("Enter ISBN number");
        ta_genre.setPromptText("Enter genre");
        tf_quantity.setPromptText("Enter quantity available");
        tf_publisher.setPromptText("Enter publisher name");
        tf_published_date.setPromptText("Enter published date");
        ta_award.setPromptText("Enter awards if any");
        tf_num_ratings.setPromptText("Enter number of ratings");
        tf_cover_image.setPromptText("Enter cover image URL or path");
    }

    private void handleAdd() {

        if (!isDouble(tf_rating.getText())) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Rating must be a valid number.");
            return;
        }

        if (!isInt(tf_quantity.getText())) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be a valid integer.");
            return;
        }

        if (!isInt(tf_num_ratings.getText())) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Number of Ratings must be a valid integer.");
            return;
        }

        if (checkEmptyFields()) {
            document = new Document(
                    documentID,
                    tf_title.getText(),
                    tf_author.getText(),
                    Double.parseDouble(tf_rating.getText()),
                    ta_description.getText(),
                    tf_language.getText(),
                    tf_isbn.getText(),
                    formatInput(ta_genre.getText()),
                    Integer.parseInt(tf_quantity.getText()),
                    tf_publisher.getText(),
                    tf_published_date.getText(),
                    formatInput(ta_award.getText()),
                    Integer.parseInt(tf_num_ratings.getText()),
                    tf_cover_image.getText()
            );
            if (documentDAO.add(document)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Adding document successfully");
                String newDocumentID = genID.generateDocumentID();
                tf_document_id.setText(newDocumentID);
                tf_document_id.setEditable(false);
            } else {
                showAlert(Alert.AlertType.ERROR, "Fail", "Error");
            }
        }
    }

    private boolean checkEmptyFields() {

        List<Control> inputFields = new ArrayList<>();

        inputFields.add(tf_title);
        inputFields.add(tf_author);
        inputFields.add(tf_rating);
        inputFields.add(tf_language);
        inputFields.add(tf_isbn);
        inputFields.add(tf_quantity);
        inputFields.add(tf_publisher);
        inputFields.add(tf_published_date);
        inputFields.add(tf_num_ratings);
        inputFields.add(tf_cover_image);

        inputFields.add(ta_description);
        inputFields.add(ta_genre);
        inputFields.add(ta_award);

        List<String> emptyFields = new ArrayList<>();

        for (Control control : inputFields) {
            String value = "";
            if (control instanceof TextField) {
                value = ((TextField) control).getText().trim();
            } else if (control instanceof TextArea) {
                value = ((TextArea) control).getText().trim();
            }

            if (value.isEmpty()) {
                emptyFields.add(control.getAccessibleText() != null
                        ? control.getAccessibleText()
                        : "Unnamed Field");
            }
        }

        if (!emptyFields.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Empty Field(s) Found");
            return false;
        }

        return true;
    }


    private void handleDeleteAll() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Delete");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to clear all fields?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                tf_title.clear();
                tf_author.clear();
                tf_rating.clear();
                ta_description.clear();
                tf_language.clear();
                tf_isbn.clear();
                ta_genre.clear();
                tf_quantity.clear();
                tf_publisher.clear();
                tf_published_date.clear();
                ta_award.clear();
                tf_num_ratings.clear();
                tf_cover_image.clear();

                showAlert(Alert.AlertType.INFORMATION, "Fields Cleared", "All fields have been cleared successfully.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Action Cancelled", "No fields were cleared.");
            }
        });
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

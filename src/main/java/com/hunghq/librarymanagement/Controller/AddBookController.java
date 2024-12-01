package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Global.GenerateDocumentID;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Respository.DocumentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

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
    private TextField tf_description;
    @FXML
    private TextField tf_language;
    @FXML
    private TextField tf_isbn;
    @FXML
    private TextField tf_genre;
    @FXML
    private TextField tf_quantity;
    @FXML
    private TextField tf_publisher;
    @FXML
    private TextField tf_published_date;
    @FXML
    private TextField tf_award;
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
        tf_description.setPromptText("Enter book description");
        tf_language.setPromptText("Enter language");
        tf_isbn.setPromptText("Enter ISBN number");
        tf_genre.setPromptText("Enter genre");
        tf_quantity.setPromptText("Enter quantity available");
        tf_publisher.setPromptText("Enter publisher name");
        tf_published_date.setPromptText("Enter published date");
        tf_award.setPromptText("Enter awards if any");
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
                    tf_description.getText(),
                    tf_language.getText(),
                    tf_isbn.getText(),
                    tf_genre.getText(),
                    Integer.parseInt(tf_quantity.getText()),
                    tf_publisher.getText(),
                    tf_published_date.getText(),
                    tf_award.getText(),
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

        List<TextField> textFields = new ArrayList<>();

        textFields.add(tf_title);
        textFields.add(tf_author);
        textFields.add(tf_rating);
        textFields.add(tf_description);
        textFields.add(tf_language);
        textFields.add(tf_isbn);
        textFields.add(tf_genre);
        textFields.add(tf_quantity);
        textFields.add(tf_publisher);
        textFields.add(tf_published_date);
        textFields.add(tf_award);
        textFields.add(tf_num_ratings);
        textFields.add(tf_cover_image);

        List<String> emptyFields = new ArrayList<>();

        for (TextField textField : textFields) {
            if (textField.getText().trim().isEmpty()) {
                emptyFields.add(textField.getPromptText());
            }
        }

        if (!emptyFields.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Empty Field Found");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void handleDeleteAll() {
        tf_title.clear();
        tf_author.clear();
        tf_rating.clear();
        tf_description.clear();
        tf_language.clear();
        tf_isbn.clear();
        tf_genre.clear();
        tf_quantity.clear();
        tf_publisher.clear();
        tf_published_date.clear();
        tf_award.clear();
        tf_num_ratings.clear();
        tf_cover_image.clear();
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
            Double.parseDouble(value); // Cố gắng chuyển đổi chuỗi thành kiểu double
            return true; // Nếu không có lỗi, giá trị là kiểu double hợp lệ
        } catch (NumberFormatException e) {
            return false; // Nếu có lỗi, giá trị không phải là double
        }
    }

    private boolean isInt(String value) {
        try {
            Integer.parseInt(value); // Cố gắng chuyển đổi chuỗi thành kiểu int
            return true; // Nếu không có lỗi, giá trị là kiểu int hợp lệ
        } catch (NumberFormatException e) {
            return false; // Nếu có lỗi, giá trị không phải là int
        }
    }

}

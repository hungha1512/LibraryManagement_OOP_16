package com.hunghq.librarymanagement.Controller;
import com.hunghq.librarymanagement.Global.AppProperties;
import com.hunghq.librarymanagement.Model.Entity.User;
import com.hunghq.librarymanagement.Model.Enum.EGender;
import com.hunghq.librarymanagement.Respository.UserDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class MainUserController {

    @FXML
    private TextField txt_user_name;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_phone;
    @FXML
    private TextField txt_gender;
    @FXML
    private TextField txt_date_of_birth;

    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_edit_password;

    private UserDAO userDAO;


    @FXML
    public void initialize() {
        userDAO = new UserDAO();

        initializeTextFields();

        btn_update.setVisible(false);
        btn_edit.setOnAction(e -> {editable();});
        btn_update.setOnAction(e -> {updateUserInformation();});
        btn_edit_password.setOnAction(e -> {openForgotPassword();});

    }

    private void initializeTextFields() {
        txt_user_name.setPromptText(AppProperties.getProperty("user.fullName"));
        txt_email.setPromptText(AppProperties.getProperty("user.email"));
        txt_phone.setPromptText(AppProperties.getProperty("user.phone"));
        txt_gender.setPromptText(AppProperties.getProperty("user.gender"));
        txt_date_of_birth.setPromptText(AppProperties.getProperty("user.dateOfBirth"));

        txt_user_name.setEditable(false);
        txt_email.setEditable(false);
        txt_phone.setEditable(false);
        txt_gender.setEditable(false);
        txt_date_of_birth.setEditable(false);
    }

    private void editable() {
        txt_user_name.setEditable(true);
        txt_email.setEditable(true);
        txt_phone.setEditable(true);
        txt_gender.setEditable(true);
        txt_date_of_birth.setEditable(true);

        btn_edit.setVisible(false);
        btn_update.setVisible(true);

        txt_user_name.setText(AppProperties.getProperty("user.fullName"));
        txt_email.setText(AppProperties.getProperty("user.email"));
        txt_phone.setText(AppProperties.getProperty("user.phone"));
        txt_gender.setText(AppProperties.getProperty("user.gender"));
        txt_date_of_birth.setText(AppProperties.getProperty("user.dateOfBirth"));
    }

    private void updateUserInformation() {

        String userName = txt_user_name.getText();
        String email = txt_email.getText();
        String phone = txt_phone.getText();
        String gender = txt_gender.getText();
        String dateOfBirth = txt_date_of_birth.getText();

        try {
            User updatedUser = new User();

            updatedUser.setUserId(Integer.parseInt(AppProperties.getProperty("user.userId")));
            updatedUser.setFullName(userName);
            updatedUser.setEmail(email);
            updatedUser.setPhone(phone);
            updatedUser.setGender(EGender.fromValue(gender));
            updatedUser.setDateOfBirth(LocalDate.parse(dateOfBirth).atStartOfDay());

            boolean isUpdated = userDAO.update(updatedUser);

            if (isUpdated) {
                showAlert("Success", "User information updated successfully!", Alert.AlertType.INFORMATION);
                initializeTextFields();
                btn_edit.setVisible(true);
                btn_update.setVisible(false);
            } else {
                showAlert("Error", "Failed to update user information. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred while updating user information: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openForgotPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hunghq/librarymanagement/View/ForgotPassword.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Forgot Password");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load ForgotPassword screen", Alert.AlertType.ERROR);
        }
    }
}

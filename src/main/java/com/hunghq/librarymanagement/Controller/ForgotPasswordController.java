package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the "Forgot Password" screen, allowing users to request a password reset.
 * Sends an email with an OTP for further authentication and opens an OTP entry modal.
 */
public class ForgotPasswordController {

    /** TextField for user to enter their email address for password recovery. */
    @FXML
    private TextField txt_email;

    /** Label to display the status of the email sending process. */
    @FXML
    private Label lbl_status;

    /**
     * Handles the process of sending a password recovery email with an OTP.
     * Verifies that the email field is not empty, then triggers the AuthenticationService
     * to send the email. Opens the OTP entry modal on successful email sending.
     */
    @FXML
    private void handleSendEmail() {
        String email = txt_email.getText();

        try {
            if (email.isEmpty()) {
                lbl_status.setText("Please enter a valid email address");
                return;
            }

            AuthenticationService.forgotPassword(email);

            lbl_status.setText("Email Sent");

            showEnterOtp(email);

            Stage stage = (Stage) txt_email.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            lbl_status.setText("Error: " + e.getMessage());
        }
    }

    /**
     * Opens the Enter OTP modal to allow the user to verify their email with an OTP.
     *
     * @param email the email address associated with the OTP
     */
    private void showEnterOtp(String email) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hunghq/librarymanagement/View/EnterOtp.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setTitle("Enter OTP");
            stage.setScene(scene);

            EnterOtpController enterOtpController = fxmlLoader.getController();
            enterOtpController.setEmail(email);

            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

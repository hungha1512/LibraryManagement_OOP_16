package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.LibraryApplication;
import com.hunghq.librarymanagement.Service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the "Forgot Password" screen, allowing users to request a password reset.
 * Sends an email with an OTP for further authentication and opens an OTP entry modal.
 */
public class ForgotPasswordController implements Initializable {

    @FXML
    public Button btn_send_email;
    /** TextField for user to enter their email address for password recovery. */
    @FXML
    private TextField txt_email;

    /** Label to display the status of the email sending process. */
    @FXML
    private Label lbl_status;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txt_email.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSendEmail();
            }
        });

        btn_send_email.setOnAction(_ -> {
            handleSendEmail();
        });
    }

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
            stage.getIcons().add(new Image(Objects.requireNonNull(LibraryApplication.class.getResourceAsStream("/com/hunghq/librarymanagement/Media/logo_uet_rm.png"))));

            EnterOtpController enterOtpController = fxmlLoader.getController();
            enterOtpController.setEmail(email);

            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

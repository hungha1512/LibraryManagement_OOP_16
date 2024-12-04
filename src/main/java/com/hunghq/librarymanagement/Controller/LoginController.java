package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Global.AppProperties;
import com.hunghq.librarymanagement.Global.Validation;
import com.hunghq.librarymanagement.LibraryApplication;
import com.hunghq.librarymanagement.Model.Entity.User;
import com.hunghq.librarymanagement.Service.AuthenticationService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Controller for the Login screen, managing user login actions, password visibility, and "Forgot Password" flow.
 */
public class LoginController implements Initializable {

    /**
     * TextField for the user to enter their username or email.
     */
    @FXML
    private TextField txt_username;

    /**
     * PasswordField for entering the user's password.
     */
    @FXML
    private PasswordField pwd_password;

    /**
     * TextField for showing the password in plain text when visible.
     */
    @FXML
    private TextField txt_password_visible;

    /**
     * Button to toggle password visibility between hidden and visible.
     */
    @FXML
    private Button btn_toggle_password;

    /**
     * Button to submit the login request.
     */
    @FXML
    private Button btn_sign_in;

    /**
     * Label to trigger the "Forgot Password" flow.
     */
    @FXML
    private Label hl_forgot_password;

    /**
     * Checkbox for "Remember Me" functionality.
     */
    @FXML
    private CheckBox cb_remember;

    /**
     * Label to display authentication status or errors.
     */
    @FXML
    private Label lbl_authenticate;

    /**
     * Tracks whether the password is currently visible.
     */
    private boolean isPasswordVisible = false;

    /**
     * Toggles the visibility of the password between hidden (password field) and visible (plain text field).
     */
    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            txt_password_visible.setVisible(true);
            txt_password_visible.setManaged(true);
            pwd_password.setVisible(false);
            pwd_password.setManaged(false);
            btn_toggle_password.setText("Hide");
        } else {
            txt_password_visible.setVisible(false);
            txt_password_visible.setManaged(false);
            pwd_password.setVisible(true);
            pwd_password.setManaged(true);
            btn_toggle_password.setText("Show");
        }
    }

    /**
     * Initializes the login form, setting up password visibility and handling
     * automatic login if the user is remembered.
     *
     * @param url            the location of the FXML file
     * @param resourceBundle resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txt_password_visible.textProperty().bindBidirectional(pwd_password.textProperty());
        Platform.runLater(() -> {
            Properties prop = new Properties();
            try {
                if (AuthenticationService.authenticateFromFile(prop)) {

                    Stage stage = (Stage) btn_sign_in.getScene().getWindow();
                    stage.close();

                    loadHomepage();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        btn_sign_in.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                handleLogin(event);
            }
        });

        txt_username.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin(event);
            }
        });

        pwd_password.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin(null);
            }
        });

        txt_password_visible.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin(null);
            }
        });
    }

    private void handleLogin(Event event) {
        String username = txt_username.getText();
        String password = pwd_password.getText();

        try {
            User userLogin = new User();
            if (Validation.isEmailValid(username)) {
                userLogin.setEmail(username);
                userLogin.setPasswordHash(password);
            }
            if (Validation.isPhoneNumberValid(username)) {
                userLogin.setPhone(username);
                userLogin.setPasswordHash(password);
            }

            lbl_authenticate.setText("");
            if (username.isEmpty()) throw new Exception("Username cannot be empty");
            if (password.isEmpty()) throw new Exception("Password cannot be empty");

            if (AuthenticationService.login(userLogin)) {
                AppProperties.setProperty("user.isRemember", cb_remember.isSelected() ? "true" : "false");

                if (event != null) {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                }

                loadHomepage();
            } else throw new Exception("Invalid username or password");
        } catch (Exception e) {
            lbl_authenticate.visibleProperty().set(true);
            lbl_authenticate.setStyle("-fx-text-fill: #f63838");
            lbl_authenticate.setText(e.getMessage());//Notification
        }
    }

    /**
     * Loads the main menu view upon successful login.
     */
    private void loadHomepage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/com/hunghq/librarymanagement/View/Homepage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Main Menu");
            stage.getIcons().add(new Image(Objects.requireNonNull(LibraryApplication.class.getResourceAsStream("/com/hunghq/librarymanagement/Media/logo_uet_rm.png"))));
            stage.setScene(scene);
            stage.show();
            Stage loginStage = (Stage) btn_sign_in.getScene().getWindow();
            if (loginStage.isShowing()) {
                loginStage.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Main Menu: " + e.getMessage());
        }
    }

    /**
     * Handles the "Forgot Password" event, opening the Forgot Password screen.
     *
     * @param mouseEvent the mouse event that triggers the forgot password action
     */
    @FXML
    public void handleForgotPassword(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/com/hunghq/librarymanagement/View/ForgotPassword.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Forgot Password");
            stage.setScene(scene);
            stage.getIcons().add(new Image(Objects.requireNonNull(LibraryApplication.class.getResourceAsStream("/com/hunghq/librarymanagement/Media/logo_uet_rm.png"))));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

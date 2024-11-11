package com.hunghq.librarymanagement.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for a confirmation dialog, allowing the user to confirm or cancel an action.
 */
public class ConfirmNotificationController implements Initializable {

    /**
     * Label to display the confirmation message.
     */
    @FXML
    private Label lbl_message;

    /**
     * Button to confirm the action.
     */
    @FXML
    private Button btn_confirm;

    /**
     * Button to cancel the action.
     */
    @FXML
    private Button btn_cancel;

    /**
     * Flag indicating whether the action has been confirmed.
     */
    private boolean isConfirmed = false;

    /**
     * The stage for the confirmation dialog window.
     */
    private Stage dialogStage;

    /**
     * Sets the stage for the confirmation dialog.
     *
     * @param dialogStage the stage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Returns the confirmation status.
     *
     * @return true if the action was confirmed, false otherwise
     */
    public boolean getIsConfirmed() {
        return isConfirmed;
    }

    /**
     * Sets the confirmation message to be displayed in the dialog.
     *
     * @param message the message to display
     */
    public void setMessage(String message) {
        this.lbl_message.setText(message);
    }

    /**
     * Hides the cancel button, useful for dialogs where only confirmation is needed.
     */
    public void hideCancelButton() {
        btn_cancel.setVisible(false);
    }

    /**
     * Initializes the controller, setting up button actions for confirm and cancel.
     * If the confirm button is clicked, the action is confirmed and the dialog is closed.
     * If the cancel button is clicked, the action is canceled and the dialog is closed.
     *
     * @param location  the location used to resolve relative paths for the root object, or null if unknown
     * @param resources the resources used to localize the root object, or null if not needed
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_confirm.setOnAction(event -> {
            isConfirmed = true;
            dialogStage.close();
        });
        btn_cancel.setOnAction(event -> {
            isConfirmed = false;
            dialogStage.close();
        });
    }
}

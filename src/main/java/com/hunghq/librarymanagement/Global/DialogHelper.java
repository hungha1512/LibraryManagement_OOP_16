package com.hunghq.librarymanagement.Global;

import com.hunghq.librarymanagement.Controller.ConfirmNotificationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for creating and displaying different types of dialog windows,
 * including confirmation and notification dialogs.
 */
public class DialogHelper {

    /**
     * Creates a new dialog stage with the specified title and loads the FXML layout.
     *
     * @param title      the title of the dialog window
     * @param fxmlLoader the FXMLLoader for loading the FXML layout
     * @return the configured Stage object for the dialog
     * @throws IOException if an error occurs while loading the FXML file
     */
    private static Stage createDialogStage(String title, FXMLLoader fxmlLoader) throws IOException {
        Parent root = fxmlLoader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        return dialogStage;
    }

    /**
     * Displays a dialog with the specified title and message. This dialog can be configured
     * to show or hide the cancel button based on the hideCancelButton parameter.
     *
     * @param title           the title of the dialog
     * @param message         the message to display in the dialog
     * @param hideCancelButton if true, hides the cancel button in the dialog
     * @return true if the user confirmed the action, false otherwise
     */
    private static boolean showDialog(String title, String message, boolean hideCancelButton) {
        boolean isConfirmed = false;
        try {
            FXMLLoader loader = new FXMLLoader(DialogHelper.class
                    .getResource("/com/hunghq/librarymanagement/View/Notification/ConfirmNotification.fxml"));

            Stage dialogStage = createDialogStage(title, loader);

            ConfirmNotificationController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMessage(message);

            if (hideCancelButton) {
                controller.hideCancelButton();
            }

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

            isConfirmed = controller.getIsConfirmed();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return isConfirmed;
    }

    /**
     * Shows a confirmation dialog with the specified title and message.
     *
     * @param title   the title of the dialog
     * @param message the message to display
     * @return true if the user confirms the action, false otherwise
     */
    public static boolean showConfirmationDialog(String title, String message) {
        return showDialog(title, message, false);
    }

    /**
     * Shows a notification dialog with the specified title and message. The cancel button is hidden.
     *
     * @param title   the title of the dialog
     * @param message the message to display
     */
    public static void showNotificationDialog(String title, String message) {
        showDialog(title, message, true);
    }
}

package com.hunghq.librarymanagement.Controller.Manage;

import com.hunghq.librarymanagement.Controller.BaseController;
import com.hunghq.librarymanagement.Controller.MethodInterceptor;
import com.hunghq.librarymanagement.Model.Annotation.RolePermissionRequired;
import com.hunghq.librarymanagement.Model.Entity.Document;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DocumentDetailFormController extends BaseController {
    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_cancel;
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_title;
    @FXML
    private TextField tf_author;
    @FXML
    private TextField tf_description;

    private MethodInterceptor methodInterceptor;
    private Stage stage;

    private Document document;

    public void setDocumentText(Document document) {
        this.document = document;

        tf_id.setText(document.getDocumentId());
        tf_title.setText(document.getTitle());
        tf_author.setText(document.getAuthor());
        tf_description.setText(document.getDescription());
    }

    private DocumentDetailController documentDetailController;

    public void setDocumentDetailController(DocumentDetailController documentDetailController) {
        this.documentDetailController = documentDetailController;
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        methodInterceptor = new MethodInterceptor(this);
        btn_cancel.setOnAction(event -> {
            if (stage != null) {
                stage.close();
            }
        });
    }

    @RolePermissionRequired(roles = {"Librarian"})
    public void handleEditDocument(ActionEvent event) {
        
    }
}

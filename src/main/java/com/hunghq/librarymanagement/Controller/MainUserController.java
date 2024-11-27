package com.hunghq.librarymanagement.Controller;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.fxml.FXML;

public class MainUserController {
    @FXML
    private TextField txt_user_name;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_phone_number;

    @FXML
    private Button btn_update_info;

    @FXML
    private Label lbl_returned_book_area;

    @FXML
    private Label lbl_notifications_area;

    @FXML
    public void initialize() {
        
    }
}

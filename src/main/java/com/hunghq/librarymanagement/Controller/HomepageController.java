package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Global.AppProperties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {


    @FXML
    public Button btn_all_books;

    @FXML
    public Button btn_recent;

    @FXML
    public Button btn_my_books;

    @FXML
    public Button btn_favorites;

    @FXML
    public Button btn_service;

    @FXML
    public Button btn_user;

    @FXML
    public Label txt_name_user;

    @FXML
    public ComboBox<String> cb_search_option;

    @FXML
    public StackPane main_screen;

    @FXML
    public Button btn_log_out;

    public void setContent(Parent newContentPane) {
        main_screen.getChildren().clear();
        main_screen.getChildren().add(newContentPane);
    }

    public void getContentPane(String fileFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileFXML));
            Parent newContentPane = loader.load();
            setContent(newContentPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Book name", "ISBN", "Genre", "Author"
        );
        cb_search_option.setItems(options);

        cb_search_option.getSelectionModel().selectFirst();

        cb_search_option.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    System.out.println("New value: " + newValue);
                }
        );

        txt_name_user.setText("Welcome back, " + AppProperties.getProperty("user.fullName") + "!");

        //TODO: Write logout controller

        getContentPane("/com/hunghq/librarymanagement/View/AllBooks/MainAllBooks.fxml");

        btn_all_books.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getContentPane("/com/hunghq/librarymanagement/View/AllBooks/MainAllBooks.fxml");
            }
        });
    }
}

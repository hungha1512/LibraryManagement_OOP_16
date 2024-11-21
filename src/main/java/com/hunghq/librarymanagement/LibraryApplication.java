package com.hunghq.librarymanagement;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LibraryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(LibraryApplication.class
                .getResource("/com/hunghq/librarymanagement/View/Login.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("UET Library Management System!");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}

package com.hunghq.librarymanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LibraryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(LibraryApplication.class
                .getResource("/com/hunghq/librarymanagement/View/Login.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("UET Library Management System!");
        stage.getIcons().add(new Image(Objects.requireNonNull(LibraryApplication.class.getResourceAsStream("/com/hunghq/librarymanagement/Media/logo_uet_rm.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

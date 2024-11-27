package com.hunghq.librarymanagement.Service;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class ImportDataToTableViewService {
    public static <T> void loadDataToTableView(TableView<T> tableView, Task<ObservableList<T>> dataTask) {

        dataTask.setOnSucceeded(event -> tableView.setItems(dataTask.getValue()));

        dataTask.setOnFailed(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading data");
            alert.setContentText("There was an error while loading data.");
            alert.showAndWait();
        });

        Thread dataThread = new Thread(dataTask);
        dataThread.setDaemon(true);
        dataThread.start();
    }
}

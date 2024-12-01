package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Global.AppProperties;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Respository.DocumentDAO;
import com.hunghq.librarymanagement.Service.AuthenticationService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomepageController extends BaseController implements Initializable {
    @FXML
    public Button btn_all_books;

    @FXML
    public Button btn_my_books;

    @FXML
//    @RolePermissionRequired(roles = {"Librarian"})
    public Button btn_manage;

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

    @FXML
    public TextField tf_search_bar;

    @FXML
    public Button btn_search;

    @FXML
    public Button btn_ask_ai;

    @FXML
    public VBox vb_navigator;

    private DocumentDAO documentDAO = new DocumentDAO();

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

        getContentPane("/com/hunghq/librarymanagement/View/AllBooks/MainAllBooks.fxml");

        setMouseCursorStyle(btn_all_books);
        setMouseCursorStyle(btn_my_books);
        setMouseCursorStyle(btn_ask_ai);
        setMouseCursorStyle(btn_manage);
        setMouseCursorStyle(btn_user);
        setMouseCursorStyle(btn_search);
        setMouseCursorStyle(btn_log_out);

        btn_all_books.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getContentPane("/com/hunghq/librarymanagement/View/AllBooks/MainAllBooks.fxml");
            }
        });

        btn_my_books.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getContentPane("/com/hunghq/librarymanagement/View/MyBooks/MainMyBooks.fxml");
            }
        });

        btn_ask_ai.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getContentPane("/com/hunghq/librarymanagement/View/AskAI/AskAI.fxml");
            }
        });
        if (AppProperties.getProperty("user.roleTitle").equals("Librarian")) {
            btn_manage.setVisible(true);
            btn_manage.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    getContentPane("/com/hunghq/librarymanagement/View/Manage/MainManage.fxml");
                }
            });
        } else {
            btn_manage.setVisible(false);
        }



        btn_user.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getContentPane("/com/hunghq/librarymanagement/View/User/MainUser.fxml");
            }
        });

        btn_search.setOnAction(actionEvent -> handleSearch());

        btn_log_out.setOnAction(actionEvent -> {
            AuthenticationService.logout();
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/hunghq/librarymanagement/View/Login.fxml")
            );
            Platform.runLater(() -> {
                Stage stage = (Stage) btn_log_out.getScene().getWindow();
                stage.close();
            });
            try {
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("UET Library Management System");
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        tf_search_bar.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearch();
            }
        });
    }

    private void handleSearch() {
        String query = tf_search_bar.getText().trim();
        String searchOption = cb_search_option.getValue();
        ObservableList<Document> searchResults = FXCollections.observableArrayList();
        if (query.isEmpty()) {
            searchResults = documentDAO.getAll();
        } else {
            switch (searchOption) {
                case "Book name":
                    searchResults = documentDAO.findByName(query);
                    break;
                case "ISBN":
                    searchResults = documentDAO.searchByISBN(query);
                    break;
                case "Genre":
                    searchResults = documentDAO.searchByGenre(query);
                    break;
                case "Author":
                    searchResults = documentDAO.searchByAuthor(query);
                    break;
                default:
                    System.out.println("Invalid search option!");
            }
        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hunghq/librarymanagement/View/AllBooks/MainAllBooks.fxml"));
            Parent newContentPane = loader.load();
            MainAllBooksController controller = loader.getController();
            controller.updateBooks(searchResults);
            setContent(newContentPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMouseCursorStyle(javafx.scene.Node node) {
        node.setOnMouseEntered(event -> node.setCursor(Cursor.HAND));
        node.setOnMouseExited(event -> node.setCursor(Cursor.DEFAULT));
    }
}

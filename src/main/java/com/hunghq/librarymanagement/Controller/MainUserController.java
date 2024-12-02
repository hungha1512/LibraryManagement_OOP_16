package com.hunghq.librarymanagement.Controller;
import com.hunghq.librarymanagement.Global.AppProperties;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Model.Entity.User;
import com.hunghq.librarymanagement.Model.Enum.EGender;
import com.hunghq.librarymanagement.Respository.DocumentDAO;
import com.hunghq.librarymanagement.Respository.UserDAO;
import com.hunghq.librarymanagement.Service.CallAPIService;
import com.hunghq.librarymanagement.Service.LoadImageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.time.LocalDate;

import static com.hunghq.librarymanagement.Global.Format.isValidDate;

public class MainUserController {

    @FXML
    public GridPane gp_returned_books;
    @FXML
    public Label lbl_page_info;

    @FXML
    public ComboBox<String> cb_search_option;
    @FXML
    public TextField tf_search_bar;
    @FXML
    public Button btn_search;

    @FXML
    private TextField txt_user_name;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_phone;
    @FXML
    private TextField txt_date_of_birth;

    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_edit_password;
    @FXML
    private Button btn_previous;
    @FXML
    private Button btn_next;

    @FXML
    private RadioButton rb_male;
    @FXML
    private RadioButton rb_female;

    @FXML
    private AnchorPane main_screen;

    private DocumentDAO documentDAO;
    private UserDAO userDAO;
    private CallAPIService callAPIService;
    private LoadImageService loadImageService;


    private int currentPage = 1;
    private int limit = 10;
    private int totalPages;
    private ObservableList<Document> returnedBooks;
    private ObservableList<Document> searchResults = FXCollections.observableArrayList();

    private ToggleGroup toggleGroup;

    @FXML
    public void initialize() {
        documentDAO = new DocumentDAO();
        userDAO = new UserDAO();
        callAPIService = new CallAPIService();
        loadImageService = new LoadImageService(callAPIService);

        toggleGroup = new ToggleGroup();
        rb_male.setToggleGroup(toggleGroup);
        rb_female.setToggleGroup(toggleGroup);

        initializeContent();

        btn_update.setVisible(false);
        btn_edit.setOnAction(e -> {
            editable();
        });
        btn_update.setOnAction(e -> {
            updateUserInformation();
        });
        btn_edit_password.setOnAction(e -> {
            openForgotPassword();
        });

        setComboBox();

        updateBooks(searchResults);
        loadBooks();

        btn_previous.setOnAction(event -> {
            if (currentPage > 1) {
                currentPage--;
                displayPage(returnedBooks);
                updatePaginationButtons();
            }
        });

        btn_next.setOnAction(event -> {
            if (!returnedBooks.isEmpty()) {
                currentPage++;
                displayPage(returnedBooks);
                updatePaginationButtons();
            }
        });

        btn_search.setOnAction(_ -> handleSearch());

        tf_search_bar.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearch();
            }
        });

    }

    private void setComboBox() {
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
    }

    private void initializeContent() {
        txt_user_name.setPromptText(AppProperties.getProperty("user.fullName"));
        txt_email.setPromptText(AppProperties.getProperty("user.email"));
        txt_phone.setPromptText(AppProperties.getProperty("user.phone"));
        txt_date_of_birth.setPromptText(AppProperties.getProperty("user.dateOfBirth") + " (YYYY-MM-DD)");

        if(AppProperties.getProperty("user.gender") == "MALE") {
            rb_male.setSelected(true);
        } else {
            rb_female.setSelected(true);
        }

        rb_female.setDisable(true);
        rb_male.setDisable(true);

        txt_user_name.setEditable(false);
        txt_email.setEditable(false);
        txt_phone.setEditable(false);
        txt_date_of_birth.setEditable(false);
    }

    private void editable() {
        txt_user_name.setEditable(true);
        txt_email.setEditable(true);
        txt_phone.setEditable(true);

        txt_date_of_birth.setEditable(true);

        btn_edit.setVisible(false);
        btn_update.setVisible(true);

        rb_male.setDisable(false);
        rb_female.setDisable(false);

        txt_user_name.setText(AppProperties.getProperty("user.fullName"));
        txt_email.setText(AppProperties.getProperty("user.email"));
        txt_phone.setText(AppProperties.getProperty("user.phone"));

        txt_date_of_birth.setText(AppProperties.getProperty("user.dateOfBirth"));
    }

    private void updateUserInformation() {
        String userName = txt_user_name.getText();
        String email = txt_email.getText();
        String phone = txt_phone.getText();
        String dateOfBirth = txt_date_of_birth.getText();
        String gender;

        if (isValidDate(dateOfBirth)) {
            showAlert("Success", "The date is valid!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Invalid date format. Please use YYYY-MM-DD.", Alert.AlertType.ERROR);
        }

        if (rb_male.isSelected()) {
            gender = "male";
        } else {
            gender = "female";
        }

        try {
            User updatedUser = new User();

            updatedUser.setUserId(Integer.parseInt(AppProperties.getProperty("user.userId")));
            updatedUser.setFullName(userName);
            updatedUser.setEmail(email);
            updatedUser.setPhone(phone);
            updatedUser.setGender(EGender.fromValue(gender));
            updatedUser.setDateOfBirth(LocalDate.parse(dateOfBirth).atStartOfDay());

            boolean isUpdated = userDAO.update(updatedUser);

            if (isUpdated) {
                showAlert("Success", "User information updated successfully!", Alert.AlertType.INFORMATION);
                initializeContent();
                btn_edit.setVisible(true);
                btn_update.setVisible(false);
            } else {
                showAlert("Error", "Failed to update user information. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openForgotPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hunghq/librarymanagement/View/ForgotPassword.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Forgot Password");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load ForgotPassword screen", Alert.AlertType.ERROR);
        }
    }

    private void loadBooks() {
        returnedBooks = documentDAO.getDocumentsWithReturnedState();
        totalPages = (int) Math.ceil((double) returnedBooks.size() / limit);
        displayPage(returnedBooks);
        updatePaginationButtons();
    }

    public void updateBooks(ObservableList<Document> searchResults) {
        returnedBooks = searchResults;
        totalPages = (int) Math.ceil((double) searchResults.size() / limit);
        currentPage = 1;
        displayPage(returnedBooks);
        updatePaginationButtons();
    }

    private void displayPage(ObservableList<Document> books) {
        gp_returned_books.getChildren().clear();

        int startIndex = (currentPage - 1) * limit;
        int endIndex = Math.min(startIndex + limit, books.size());

        int column = 0;
        int row = 0;

        for (int i = startIndex; i < endIndex; i++) {
            Document document = books.get(i);

            VBox vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefHeight(150);
            vBox.setMaxHeight(150);

            ImageView imageView = new ImageView();
            imageView.setFitHeight(140);
            imageView.setFitWidth(140);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            vBox.getChildren().add(imageView);

            Label nameLabel = new Label(document.getTitle());
            nameLabel.setWrapText(true);
            nameLabel.setMaxWidth(120);
            nameLabel.setStyle("-fx-alignment: center;");
            vBox.getChildren().add(nameLabel);

            loadImageService.loadImageWithEntity(document, imageView);

            gp_returned_books.add(vBox, column, row);
            vBox.getStyleClass().add("service-box");
            imageView.getStyleClass().add("image-box");

            vBox.setOnMouseClicked(event -> {
                showBookDetail(document);
            });

            vBox.setOnMouseEntered(event -> {
                vBox.setOpacity(0.8);
                vBox.setStyle("-fx-cursor: hand;");
            });

            vBox.setOnMouseExited(event -> {
                vBox.setOpacity(1.0);
            });

            column++;
            if (column == 5) {
                column = 0;
                row++;
            }
        }

        lbl_page_info.setText("Page " + currentPage + " of " + totalPages);
    }

    private void showBookDetail(Document document) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hunghq/librarymanagement/View/AllBooks/BookDetail.fxml"));

            Scene scene = new Scene(fxmlLoader.load());

            BookDetailController bookDetailController = fxmlLoader.getController();

            bookDetailController.initialize(document);

            Stage stage = new Stage();
            stage.setTitle("Book Detail");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePaginationButtons() {
        btn_previous.setDisable(currentPage == 1);
        btn_next.setDisable(currentPage == totalPages || returnedBooks.isEmpty());
    }

    private void handleSearch() {
        String query = tf_search_bar.getText().trim();
        String searchOption = cb_search_option.getValue();

        if (query.isEmpty()) {
            searchResults = documentDAO.getDocumentsWithReturnedState();
        } else {
            switch (searchOption) {
                case "Book name":
                    searchResults = returnedBooks.filtered(document -> document.getTitle().toLowerCase().contains(query.toLowerCase()));
                    break;
                case "ISBN":
                    searchResults = returnedBooks.filtered(document -> document.getIsbn().toLowerCase().contains(query.toLowerCase()));
                    break;
                case "Genre":
                    searchResults = returnedBooks.filtered(document -> document.getGenre().toLowerCase().contains(query.toLowerCase()));
                    break;
                case "Author":
                    searchResults = returnedBooks.filtered(document -> document.getAuthor().toLowerCase().contains(query.toLowerCase()));
                    break;
                default:
            }
        }
        
        updateBooks(searchResults);

    }

}

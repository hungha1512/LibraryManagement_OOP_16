package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Global.AppProperties;
import com.hunghq.librarymanagement.Model.Entity.*;
import com.hunghq.librarymanagement.Model.Enum.EGender;
import com.hunghq.librarymanagement.Model.Enum.EIsDeleted;
import com.hunghq.librarymanagement.Model.Enum.EState;
import com.hunghq.librarymanagement.Respository.BorrowDocumentDAO;
import com.hunghq.librarymanagement.Respository.ReviewDAO;
import com.hunghq.librarymanagement.Respository.RoleDAO;
import com.hunghq.librarymanagement.Service.CallAPIService;
import com.hunghq.librarymanagement.Service.FilterGenreService;
import com.hunghq.librarymanagement.Service.LoadImageService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static java.lang.Integer.parseInt;

public class BookDetailController extends BaseController {
    @FXML
    private ImageView bookImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private Button borrowButton;

    @FXML
    private Button returnButton;

    @FXML
    private Button extendButton;

    @FXML
    private Button printButton;

    @FXML
    private Label notiLabel;


    @FXML
    private Label descriptionLabel;


    @FXML
    private Label anotherAuthorLabel;
    @FXML
    private Label publisherLabel;
    @FXML
    private Label publishedDateLabel;
    @FXML
    private Label languageLabel;

    @FXML
    private HBox hBoxStarsInactive;

    @FXML
    private Button btnStar1, btnStar2, btnStar3, btnStar4, btnStar5;

    @FXML
    private Button btnCancelRating;
    @FXML
    private TextArea commentTextArea;
    @FXML
    private Button sendButton;

    private CallAPIService callAPIService;
    private LoadImageService loadImageService;
    private FilterGenreService filterGenreService;
    private BorrowDocumentDAO borrowDocumentDAO;
    private Document document;
    private User user;
    private int rating;
    private boolean isBorrowed;
    private boolean isOverdue;

    @FXML
    public void initialize(Document document) {
        filterGenreService = new FilterGenreService();
        callAPIService = new CallAPIService();
        loadImageService = new LoadImageService(callAPIService);
        borrowDocumentDAO = new BorrowDocumentDAO();

        this.document = document;
        this.user = userBorrow();

        setContent();
        setActionButton();
        handleStarRating(0);

        isBorrowed = borrowDocumentDAO.isDocumentBorrowed(document.getDocumentId(), user.getUserId());
        isOverdue = borrowDocumentDAO.isDocumentOverdue(document.getDocumentId(), user.getUserId());

        if (isBorrowed || isOverdue) {
            visibleIfBorrow();
            setNotificationLabel(borrowDocumentDAO.getBorrowDate(document.getDocumentId(), user.getUserId()),
                    borrowDocumentDAO.getDueDate(document.getDocumentId(), user.getUserId()));
            notiLabel.setVisible(true);
        } else {
            visibleIfNotBorrow();
            notiLabel.setVisible(false);
        }
    }

    private void setContent() {
        titleLabel.setText(document.getTitle());
        authorLabel.setText(document.getAuthorName());
        genreLabel.setText(filterGenreService.formatGenres(document.getGenre()));
        descriptionLabel.setText(document.getDescription());

        anotherAuthorLabel.setText(document.getAuthorName());
        publisherLabel.setText(document.getPublisher());
        publishedDateLabel.setText(document.getPublishedDate());
        languageLabel.setText(document.getLanguage());

        loadImageService.loadImage(document, bookImageView);
    }

    private void setActionButton() {
        borrowButton.setOnAction(event -> handleBorrowButtonAction());
        returnButton.setOnAction(event -> handleReturnButtonAction());
        extendButton.setOnAction(event -> handleExtendButtonAction());

        btnStar1.setOnAction(event -> handleStarRating(1));
        btnStar2.setOnAction(event -> handleStarRating(2));
        btnStar3.setOnAction(event -> handleStarRating(3));
        btnStar4.setOnAction(event -> handleStarRating(4));
        btnStar5.setOnAction(event -> handleStarRating(5));

        sendButton.setOnAction(event -> handleSendButtonAction());
        btnCancelRating.setOnAction(event -> handelCancelRating());
    }

    private User userBorrow() {
        RoleDAO roleDAO = new RoleDAO();

        int userId = parseInt(AppProperties.getProperty("user.userId"));
        String fullName = AppProperties.getProperty("user.fullName");
        String passwordHash = AppProperties.getProperty("user.passwordHash");
        EGender gender = EGender.fromValue(AppProperties.getProperty("user.gender"));
        String email = AppProperties.getProperty("user.email");
        String phone = AppProperties.getProperty("user.phone");
        LocalDateTime joinDate = LocalDateTime.parse(AppProperties.getProperty("user.joinDate"), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        LocalDateTime dateOfBirth = LocalDateTime.parse(AppProperties.getProperty("user.dateOfBirth"), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        Role role = roleDAO.getRoleByTitle(AppProperties.getProperty("user.roleTitle"));
        String otp = AppProperties.getProperty("user.otp");
        EIsDeleted eIsDeleted = EIsDeleted.ACTIVE;

        return new User(
                userId,
                fullName,
                passwordHash,
                gender,
                email,
                phone,
                joinDate,
                dateOfBirth,
                role,
                otp,
                eIsDeleted
        );
    }

    @FXML
    private void handleBorrowButtonAction() {
        if (borrowDocumentDAO.limitOverdueDocument(user.getUserId())) {
            showAlert("Overdue Limit Exceeded",
                    "You have more than 3 overdue documents. Please return them before borrowing more.",
                    Alert.AlertType.WARNING);
        } else if (!borrowDocumentDAO.canBorrowAfterReturn(document.getDocumentId(), user.getUserId())) {
            showAlert("Cannot Borrow Yet",
                    "You have just borrowed this book.",
                    Alert.AlertType.WARNING);
        } else {
            visibleIfBorrow();
            interactWithBorrowDocumentInDB();
            showAlert("Borrow Successful",
                    "You have successfully borrowed the document.",
                    Alert.AlertType.INFORMATION);
        }
    }

    public void handleExtendButtonAction() {
        LocalDateTime extendDate = borrowDocumentDAO.getExtendDate(document.getDocumentId(), user.getUserId());

        if (extendDate != null) {
            showAlert("Extension Not Allowed",
                    "This document's due date has already been extended and cannot be extended again.",
                    Alert.AlertType.WARNING);
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Extend Borrow Period");
        confirmAlert.setHeaderText("Extend Due Date");
        confirmAlert.setContentText("Extend the due date for this document by 1 week?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean canUpdateExtendDate = borrowDocumentDAO.updateExtendDate(document.getDocumentId(), user.getUserId());
                boolean canUpdateDueDate = borrowDocumentDAO.updateDueDate(document.getDocumentId(), user.getUserId());

                if (canUpdateExtendDate && canUpdateDueDate) {
                    showAlert("Extension Successful",
                            "The due date has been successfully extended by 1 week.",
                            Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Extension Failed",
                            "Failed to extend the due date. Please try again.",
                            Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Extension Cancelled",
                        "The due date extension has been cancelled.",
                        Alert.AlertType.INFORMATION);
            }
        });
    }


    private void handleReturnButtonAction() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Return");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to return this document?");

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            processReturnDocument();
            showAlert("Return Successful", "You have successfully returned the document.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Return Cancelled", "The return process has been cancelled.", Alert.AlertType.INFORMATION);
        }
    }


    private void processReturnDocument() {
        visibleIfNotBorrow();
        notiLabel.setVisible(false);
        borrowDocumentDAO.updateBorrowDocumentStateToReturned(document.getDocumentId(), user.getUserId());
    }


    private void interactWithBorrowDocumentInDB () {

        BorrowDocument borrowDocument = new BorrowDocument(
                0,
                this.document,
                this.user,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(0),
                null,
                null,
                EState.BORROWED
        );

        borrowDocumentDAO.add(borrowDocument);
        setNotificationLabel(borrowDocumentDAO.getBorrowDate(document.getDocumentId(), user.getUserId()),
                borrowDocumentDAO.getDueDate(document.getDocumentId(), user.getUserId()));
    }

    private void visibleIfBorrow() {
        borrowButton.setVisible(false);
        returnButton.setVisible(true);
        extendButton.setVisible(true);
        printButton.setVisible(true);
        notiLabel.setVisible(true);
    }

    private void visibleIfNotBorrow() {
        borrowButton.setVisible(true);
        returnButton.setVisible(false);
        extendButton.setVisible(false);
        printButton.setVisible(false);
    }

    @FXML
    private void handleSendButtonAction() {
        ReviewDAO reviewDAO = new ReviewDAO();
        String comment = commentTextArea.getText();
        Review review = new Review(
                0,
                document,
                user,
                rating,
                comment,
                LocalDateTime.now()
        );


        if (this.rating != 0 && !comment.isEmpty()) {
            reviewDAO.add(review);
            showAlert("Success", "Review submitted successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Missing Information", "Missing information.", Alert.AlertType.WARNING);
        }
    }


    private void handelCancelRating() {
        for (int i = 1; i <= 5; i++) {
            Button starButton = (Button) hBoxStarsInactive.getChildren().get(i - 1);
            starButton.setStyle("-fx-background-color: transparent;");
            starButton.setText("☆");
            starButton.setStyle(starButton.getStyle() + "-fx-text-fill: black; -fx-font-weight: normal;");
        }
    }

    private void handleStarRating(int rating) {
        this.rating = rating;
        for (int i = 1; i <= 5; i++) {
            Button starButton = (Button) hBoxStarsInactive.getChildren().get(i - 1);
            starButton.setStyle("-fx-background-color: transparent;");

            if (i <= rating) {
                starButton.setText("★");
                starButton.setStyle(starButton.getStyle() + "-fx-text-fill: #f8c304; -fx-font-weight: bold;");
            } else {
                starButton.setText("☆");
                starButton.setStyle(starButton.getStyle() + "-fx-text-fill: black; -fx-font-weight: normal;");
            }
        }
    }
    private void setNotificationLabel(LocalDateTime borrowDate, LocalDateTime dueDate) {
        String noti = "";

        if (isBorrowed) {
            noti = "You borrowed this document at " + borrowDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            if (dueDate.isAfter(LocalDateTime.now())) {
                long daysRemaining = Duration.between(LocalDateTime.now(), dueDate).toDays();
                noti += "\nYou have " + daysRemaining + " day(s) remaining to return.";
            }
        } else if (isOverdue) {
            long daysOverdue = Duration.between(dueDate, LocalDateTime.now()).toDays();
            noti = "Overdue for " + daysOverdue + " day(s).";
        }

        notiLabel.setText(noti);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}

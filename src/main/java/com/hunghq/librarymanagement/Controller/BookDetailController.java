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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.Integer.parseInt;

public class BookDetailController extends BaseController {

    // Book details section
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

    // Description section
    @FXML
    private Label descriptionLabel;

    // Information section
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
    private TextArea commentTextArea;
    @FXML
    private Button sendButton;

    private CallAPIService callAPIService;
    private LoadImageService loadImageService;
    private FilterGenreService filterGenreService;
    private Document document;
    private User user;
    private int rating;

    // Initialize the controller
    @FXML
    public void initialize(Document document) {
        filterGenreService = new FilterGenreService();
        callAPIService = new CallAPIService();
        loadImageService = new LoadImageService(callAPIService);
        this.document = document;
        this.user = userBorrow();

        setContent();
        setActionButton();
        handleStarRating(0);
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

        btnStar1.setOnAction(event -> handleStarRating(1));
        btnStar2.setOnAction(event -> handleStarRating(2));
        btnStar3.setOnAction(event -> handleStarRating(3));
        btnStar4.setOnAction(event -> handleStarRating(4));
        btnStar5.setOnAction(event -> handleStarRating(5));

        sendButton.setOnAction(event -> handleSendButtonAction());
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
        visibleButtonAfterBorrow();
        interactWithBorrowDocumentInDB();
    }

    private void interactWithBorrowDocumentInDB () {
        BorrowDocumentDAO borrowDocumentDAO = new BorrowDocumentDAO();

        BorrowDocument borrowDocument = new BorrowDocument(
                0,
                this.document,
                this.user,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now().plusDays(14),
                null,
                EState.BORROWED
        );

        // Add condition to borrow
        borrowDocumentDAO.add(borrowDocument);
    }


    private void visibleButtonAfterBorrow() {
        borrowButton.setVisible(false);
        returnButton.setVisible(true);
        extendButton.setVisible(true);
        printButton.setVisible(true);
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
                java.time.LocalDateTime.now()
        );
        if (this.rating != 0 && !comment.isEmpty()) {
            reviewDAO.add(review);
        } else {
            // Handle if rating and comment is null
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

}

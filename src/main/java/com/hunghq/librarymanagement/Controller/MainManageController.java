package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Model.Entity.BorrowDocument;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Model.Entity.User;
import com.hunghq.librarymanagement.Model.Enum.EState;
import com.hunghq.librarymanagement.Respository.BorrowDocumentDAO;
import com.hunghq.librarymanagement.Respository.DocumentDAO;
import com.hunghq.librarymanagement.Respository.UserDAO;
import com.hunghq.librarymanagement.Service.EmailService;
import com.hunghq.librarymanagement.Service.FilterGenreService;
import com.hunghq.librarymanagement.Service.ImportDataToTableViewService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class MainManageController extends BaseController {
    @FXML
    public ListView<String> listView;
    @FXML
    public BarChart bc_books_by_genre;
    @FXML
    public PieChart pc_borrow_status;
    @FXML
    public LineChart lc_borrowing_trends;
    @FXML
//    @RolePermissionRequired(roles = {"Librarian"})
    public Button btn_add_book;
    @FXML
//    @RolePermissionRequired(roles = {"Librarian"})
    public Button btn_delete_book;
    @FXML
//    @RolePermissionRequired(roles = {"Librarian"})
    public Button btn_edit_book;
    @FXML
    public TextField tf_search_book;
    @FXML
    public Button btn_search_book;

    @FXML
    public Label lb_total_user;
    @FXML
    public Label lb_total_borrowed_book;
    @FXML
    public Label lb_total_book;
    @FXML
    public Label lb_total_overdue_book;
    @FXML
    public Button btn_previous_book;
    @FXML
    public Label lbl_page_info_book;
    @FXML
    public Button btn_next_book;
    @FXML
    public Button btn_previous_borrow;
    @FXML
    public Label lbl_page_info_borrow;
    @FXML
    public Button btn_next_borrow;
    @FXML
    public Button btn_add_user;
    @FXML
    public Button btn_delete_user;
    @FXML
    public TextField tf_search_user;
    @FXML
    public TextField tf_search_borrow;
    @FXML
    public Button btn_search_borrow;
    @FXML
    public Button btn_search_user;
    @FXML
    public Button btn_previous_user;
    @FXML
    public Label lbl_page_info_user;
    @FXML
    public Button btn_next_user;


    @FXML
    private TableColumn<Document, String> col_book_id;
    @FXML
    private TableColumn<Document, String> col_title;
    @FXML
    private TableColumn<Document, String> col_author;
    @FXML
    private TableColumn<Document, String> col_isbn;
    @FXML
    private TableColumn<Document, String> col_genre;
    @FXML
    private TableColumn<Document, Integer> col_quantity;
    @FXML
    private TableColumn<Document, String> col_publisher;
    @FXML
    private TableColumn<Document, String> col_published_date;
    @FXML
    private TableView<Document> tv_book;


    @FXML
    private TableColumn<BorrowDocument, String> col_borrow_id;
    @FXML
    private TableColumn<BorrowDocument, String> col_document_id;
    @FXML
    private TableColumn<BorrowDocument, String> col_user_id;
    @FXML
    private TableColumn<BorrowDocument, Double> col_fee;
    @FXML
    private TableColumn<BorrowDocument, String> col_borrow_date;
    @FXML
    private TableColumn<BorrowDocument, String> col_due_date;
    @FXML
    private TableColumn<BorrowDocument, String> col_return_date;
    @FXML
    private TableColumn<BorrowDocument, String> col_extend_date;
    @FXML
    private TableColumn<BorrowDocument, String> col_state;
    @FXML
    private TableView<BorrowDocument> tv_borrowDocument;

    @FXML
    private TableView<User> tv_user;
    @FXML
    private TableColumn<User, Integer> col_user_id1;
    @FXML
    private TableColumn<User, String> col_full_name;
    @FXML
    private TableColumn<User, String> col_gender;
    @FXML
    private TableColumn<User, String> col_email;
    @FXML
    private TableColumn<User, String> col_phone;
    @FXML
    private TableColumn<User, String> col_join_date;
    @FXML
    private TableColumn<User, String> col_date_of_birth;
    @FXML
    private TableColumn<User, String> col_role;


    @FXML
//    @RolePermissionRequired(roles = {"Librarian"})
    public Button btn_send_noti;
    @FXML
//    @RolePermissionRequired(roles = {"Librarian"})
    public Button btn_return_book;

    private final FilterGenreService filterGenreService = new FilterGenreService();
    private final DocumentDAO documentDAO = new DocumentDAO();
    private final BorrowDocumentDAO borrowDocumentDAO = new BorrowDocumentDAO();
    private final UserDAO userDAO = new UserDAO();

    private int currentPageBook = 1;
    private final int limit = 16;
    private int totalPagesBook;
    private List<Document> searchResultsDocument;
    private boolean isSearchingBook = false;

    private int currentPageBorrowedBook = 1;
    private int totalPagesBorrowedBook;
    private List<BorrowDocument> searchResultsBorrowedDocument;
    private boolean isSearchingBorrowedBook = false;

    private int currentPageUser = 1;
    private int totalPagesUser;
    private List<User> searchResultsUser;
    private boolean isSearchingUser = false;

    @FXML
//    @RolePermissionRequired(roles = {"Librarian"})
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        initializeBooksByGenreChart();
        initializeBorrowStatusPieChart();
        initializeBorrowingTrendsLineChart();
        initializeTotalDataView();

        isSearchingBook = false;
        initializeTableViewBook();
        isSearchingBorrowedBook = false;
        initializeTableViewBorrowBook();
        isSearchingUser = false;
        initializeTableViewUser();

        //Button on Manage Book tab
        btn_add_book.setOnAction(_ -> handleAddBook());
        btn_delete_book.setOnAction(_ -> handleDeleteBook());
        btn_edit_book.setOnAction(_ -> handleEditBook());
        btn_search_book.setOnAction(_ -> handleSearchBook());
        btn_previous_book.setOnAction(_ -> {
            if (currentPageBook > 1) {
                currentPageBook--;
                updateTableViewBook();
                updatePaginationBookButtons();
            }
        });
        btn_next_book.setOnAction(_ -> {
            if (currentPageBook < totalPagesBook) {
                currentPageBook++;
                updateTableViewBook();
                updatePaginationBookButtons();
            }
        });
        tf_search_book.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearchBook();
            }
        });

        //Button on Manage Borrow tab
        btn_send_noti.setOnAction(_ -> {
            try {
                handleSendNotification();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        btn_return_book.setOnAction(_ -> handleReturnBook());
        btn_search_borrow.setOnAction(_ -> handleSearchBorrowBook());
        btn_previous_borrow.setOnAction(_ -> {
            if (currentPageBorrowedBook > 1) {
                currentPageBorrowedBook--;
                updateTableViewBorrowedBook();
                updatePaginationBorrowBookButtons();
            }
        });
        btn_next_borrow.setOnAction(_ -> {
            if (currentPageBorrowedBook < totalPagesBorrowedBook) {
                currentPageBorrowedBook++;
                updateTableViewBorrowedBook();
                updatePaginationBorrowBookButtons();
            }
        });
        tf_search_borrow.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearchBorrowBook();
            }
        });

        //Button on Manage user tab
        btn_search_user.setOnAction(_ -> handleSearchUser());
        btn_previous_user.setOnAction(_ -> {
            if (currentPageUser > 1) {
                currentPageUser--;
                updateTableViewUser();
                updatePaginationUserButtons();
            }
        });
        btn_next_user.setOnAction(_ -> {
            if (currentPageUser < totalPagesUser) {
                currentPageUser++;
                updateTableViewUser();
                updatePaginationUserButtons();
            }
        });
        tf_search_user.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearchUser();
            }
        });
    }

    public void initializeBooksByGenreChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.setName("Top 5 Genres by Quantity");
        Set<String> genres = filterGenreService.getGenres();

        Map<String, Integer> genreCounts = new HashMap<>();

        List<Document> documents = documentDAO.getAll();
        for (Document document : documents) {
            String genreStr = document.getGenre();
            genreStr = genreStr.replace("[", "").replace("]", "").replace("'", "").trim();
            String[] genreArray = genreStr.split(",\\s*");

            for (String genre : genreArray) {
                genre = genre.trim();
                genreCounts.put(genre, genreCounts.getOrDefault(genre, 0) + document.getQuantity());
            }
        }

        List<Map.Entry<String, Integer>> topGenres = genreCounts.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .collect(Collectors.toList());

        for (Map.Entry<String, Integer> entry : topGenres) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        bc_books_by_genre.getData().add(series);
    }

    public void initializeBorrowStatusPieChart() {
        List<BorrowDocument> allBorrowDocuments = borrowDocumentDAO.getAll();

        // Đếm số lượng sách đang mượn (chưa trả) và đã trả
        long borrowedCount = allBorrowDocuments.stream()
                .filter(doc -> !doc.getState().getState().equalsIgnoreCase("returned"))
                .count();

        long returnedCount = allBorrowDocuments.stream()
                .filter(doc -> doc.getState().getState().equalsIgnoreCase("returned"))
                .count();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Borrowed", borrowedCount),
                new PieChart.Data("Returned", returnedCount)
        );

        pc_borrow_status.setData(pieChartData);

        pc_borrow_status.setTitle("Borrow Status");
        pc_borrow_status.setLegendVisible(true);
        pc_borrow_status.setLabelsVisible(true);

        for (PieChart.Data data : pieChartData) {
            data.getNode().addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, event -> {
                Tooltip.install(data.getNode(),
                        new Tooltip(data.getName() + ": " + (int) data.getPieValue() + " books"));
            });
        }
    }

    public void initializeBorrowingTrendsLineChart() {
        List<BorrowDocument> allBorrowDocuments = borrowDocumentDAO.getAll();

        Map<String, Long> borrowingTrends = allBorrowDocuments.stream()
                .filter(doc -> doc.getBorrowDate() != null)
                .collect(Collectors.groupingBy(
                        doc -> doc.getBorrowDate().toLocalDate().toString(), //Date formatter
                        TreeMap::new, // Sort list overtime
                        Collectors.counting() //
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Borrowing Trends");

        for (Map.Entry<String, Long> entry : borrowingTrends.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lc_borrowing_trends.getData().clear();
        lc_borrowing_trends.getData().add(series);

        lc_borrowing_trends.setTitle("Borrowing Trends Over Time (Daily)");
        lc_borrowing_trends.getXAxis().setLabel("Date (YYYY-MM-DD)");
        lc_borrowing_trends.getYAxis().setLabel("Number of Borrowings");
    }

    public void initializeTableViewBook() {
        col_book_id.setCellValueFactory(new PropertyValueFactory<>("documentId"));
        col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        col_published_date.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        updateTableViewBook();
    }


    public void initializeTableViewBorrowBook() {
        col_borrow_id.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        col_document_id.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDocument().getDocumentId()));
        col_user_id.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getUser().getFullName())));
        col_fee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        col_borrow_date.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        col_due_date.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        col_return_date.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        col_extend_date.setCellValueFactory(new PropertyValueFactory<>("extendDate"));
        col_state.setCellValueFactory(new PropertyValueFactory<>("state"));
        updateTableViewBorrowedBook();
    }

    public void initializeTotalDataView() {
        lb_total_book.setText(String.valueOf(documentDAO.getAll().size()));
        lb_total_user.setText(String.valueOf(userDAO.getAll().size()));
        lb_total_borrowed_book.setText(String.valueOf(borrowDocumentDAO.getBorrowedDocuments().size()
                + borrowDocumentDAO.getOverdueDocuments().size()));
        lb_total_overdue_book.setText(String.valueOf(borrowDocumentDAO.getOverdueDocuments().size()));
    }

    public void initializeTableViewUser() {
        col_user_id1.setCellValueFactory(new PropertyValueFactory<>("userId"));
        col_full_name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_join_date.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        col_date_of_birth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        col_role.setCellValueFactory(new PropertyValueFactory<>("role"));
        updateTableViewUser();
    }

    public void handleAddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hunghq/librarymanagement/View/Manage/AddBook.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Book");
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            AddBookController controller = loader.getController();
            controller.setMainManageController(this);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading AddBook.fxml: " + e.getMessage());
        }
    }


    public void handleDeleteBook() {
        Document selectedDocument = tv_book.getSelectionModel().getSelectedItem();

        if (selectedDocument == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Selection");
            alert.setContentText("Please select a document to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText("Delete Document");
        confirmAlert.setContentText("Are you sure you want to delete this document: " + selectedDocument.getTitle() + "?");

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                documentDAO.delete(selectedDocument.getDocumentId());

                tv_book.getItems().remove(selectedDocument);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Document Deleted");
                successAlert.setContentText("The document '" + selectedDocument.getTitle() + "' has been deleted.");
                successAlert.showAndWait();
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Delete Error");
                errorAlert.setContentText("Could not delete the document. Please try again.\n" + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    public void handleEditBook() {
        try {
            Document selectedDocument = tv_book.getSelectionModel().getSelectedItem();

            if (selectedDocument == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Book Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a book to edit.");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hunghq/librarymanagement/View/Manage/EditBook.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Book");
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            EditBookController controller = loader.getController();

            controller.initialize(selectedDocument);

            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading EditBook.fxml: " + e.getMessage());
        }
    }


    public void handleSearchBook() {
        String query = tf_search_book.getText().trim().toLowerCase();

        Task<List<Document>> searchTask = new Task<>() {
            @Override
            protected List<Document> call() throws Exception {
                if (query.isEmpty()) {
                    isSearchingBook = false;
                    return documentDAO.getAll();
                } else {
                    isSearchingBook = true;
                    return documentDAO.getAll().stream()
                            .filter(doc -> doc.getTitle().toLowerCase().contains(query) ||
                                    doc.getAuthor().toLowerCase().contains(query) ||
                                    doc.getGenre().toLowerCase().contains(query) ||
                                    doc.getIsbn().toLowerCase().contains(query))
                            .collect(Collectors.toList());
                }
            }
        };

        searchTask.setOnSucceeded(_ -> {
            searchResultsDocument = searchTask.getValue();
            currentPageBook = 1;
            updateTableViewBook();
        });

        searchTask.setOnFailed(_ -> {
            System.out.println("Search Task failed: " + searchTask.getException().getMessage());
        });

        new Thread(searchTask).start();
    }

    public void handleSendNotification() throws MessagingException {
        DecimalFormat df = new DecimalFormat("#");
        BorrowDocument borrowDocument = tv_borrowDocument.getSelectionModel().getSelectedItem();

        if (borrowDocument != null) {
            String subject = "UET Library Manage System - Borrow Detail";
            String body = "";

            switch (borrowDocument.getState()) {
                case EState.BORROWED:
                    body = "This is an email to notify about your borrowed book details:\n"
                            + "- Book: " + borrowDocument.getDocument().getTitle() + "\n"
                            + "- ISBN: " + borrowDocument.getDocument().getIsbn() + "\n"
                            + "- Due Date: " + borrowDocument.getDueDate().toString() + "\n"
                            + "Please return the book before the due date.";
                    break;

                case EState.OVERDUE:
                    long overdueDays = Duration.between(borrowDocument.getDueDate(), LocalDateTime.now()).toDays();
                    body = "This is an email to notify that the borrowed book is overdue:\n"
                            + "- Book: " + borrowDocument.getDocument().getTitle() + "\n"
                            + "- ISBN: " + borrowDocument.getDocument().getIsbn() + "\n"
                            + "- Due Date: " + borrowDocument.getDueDate().toString() + "\n"
                            + "- Overdue: " + overdueDays + " day(s)\n"
                            + "- Fee: " + df.format(borrowDocument.getFee()) + " VND\n"
                            + "Please return the book as soon as possible.";
                    break;

                case EState.RETURNED:
                    body = "This is an email to notify that your borrowed book has been returned successfully:\n"
                            + "- Book: " + borrowDocument.getDocument().getTitle() + "\n"
                            + "- ISBN: " + borrowDocument.getDocument().getIsbn() + "\n"
                            + "- Returned Date: " + borrowDocument.getReturnDate().toString() + "\n"
                            + "Thank you for using our library service.";
                    break;

                default:
                    body = "Unknown state. Unable to send notification.";
            }

            if (!body.equals("Unknown state. Unable to send notification.")) {
                EmailService.sendEmail(borrowDocument.getUser().getEmail(), subject, body);
            }
        }
    }


    public void handleReturnBook() {
        BorrowDocument borrowDocument = tv_borrowDocument.getSelectionModel().getSelectedItem();
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Return");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to return this document?");

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (borrowDocument != null && !borrowDocument.getState().toString().equalsIgnoreCase("returned")) {
                borrowDocumentDAO.updateBorrowDocumentStateToReturned(borrowDocument.getDocument().getDocumentId(), borrowDocument.getUser().getUserId());
                documentDAO.updateBookQuantityWhenReturn(borrowDocument.getDocument().getDocumentId(), borrowDocument.getUser().getUserId());
            }
            showAlert("Return Successful", "You have successfully returned the document.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Return Cancelled", "The return process has been cancelled.", Alert.AlertType.INFORMATION);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleSearchBorrowBook() {
        String query = tf_search_borrow.getText().trim().toLowerCase();

        Task<List<BorrowDocument>> searchTask = new Task<>() {
            @Override
            protected List<BorrowDocument> call() throws Exception {
                if (query.isEmpty()) {
                    isSearchingBorrowedBook = false;
                    return borrowDocumentDAO.getAll();
                } else {
                    isSearchingBorrowedBook = true;
                    return borrowDocumentDAO.getAll().stream()
                            .filter(doc -> String.valueOf(doc.getBorrowId()).contains(query) ||
                                    doc.getDocument().getDocumentId().toLowerCase().contains(query))
                            .collect(Collectors.toList());
                }
            }
        };

        searchTask.setOnSucceeded(_ -> {
            searchResultsBorrowedDocument = searchTask.getValue();
            currentPageBorrowedBook = 1;
            updateTableViewBorrowedBook();
        });

        searchTask.setOnFailed(_ -> {
            System.out.println("Search Task failed: " + searchTask.getException().getMessage());
        });

        new Thread(searchTask).start();
    }

    public void handleSearchUser() {
        String query = tf_search_user.getText().trim().toLowerCase();

        Task<List<User>> searchTask = new Task<>() {
            @Override
            protected List<User> call() throws Exception {
                if (query.isEmpty()) {
                    isSearchingUser = false;
                    return userDAO.getAll();
                } else {
                    isSearchingUser = true;
                    return userDAO.getAll().stream()
                            .filter(doc -> doc.getFullName().contains(query) ||
                                    doc.getPhone().toLowerCase().contains(query) ||
                                    doc.getEmail().toLowerCase().contains(query) ||
                                    String.valueOf(doc.getUserId()).contains(query))
                            .collect(Collectors.toList());
                }
            }
        };

        searchTask.setOnSucceeded(_ -> {
            searchResultsUser = searchTask.getValue();
            currentPageUser = 1;
            updateTableViewUser();
        });

        searchTask.setOnFailed(_ -> {
            System.out.println("Search Task failed: " + searchTask.getException().getMessage());
        });

        new Thread(searchTask).start();
    }

    private void updateTableViewBook() {
        List<Document> sourceList = isSearchingBook ? searchResultsDocument : documentDAO.getAll();
        totalPagesBook = (int) Math.ceil((double) sourceList.size() / limit);
        int startIndex = (currentPageBook - 1) * limit;
        int endIndex = Math.min(startIndex + limit, sourceList.size());
        lbl_page_info_book.setText("Page " + currentPageBook + " of " + totalPagesBook);
        updatePaginationBookButtons();

        Task<ObservableList<Document>> loadPaginatedBooksTask = new Task<>() {
            @Override
            protected ObservableList<Document> call() throws Exception {
                return FXCollections.observableArrayList(sourceList.subList(startIndex, endIndex));
            }
        };
        loadPaginatedBooksTask.setOnSucceeded(_ -> {
            tv_book.setItems(loadPaginatedBooksTask.getValue());
        });
        ImportDataToTableViewService.loadDataToTableView(tv_book, loadPaginatedBooksTask);
    }

    private void updateTableViewBorrowedBook() {
        List<BorrowDocument> sourceList = isSearchingBorrowedBook ? searchResultsBorrowedDocument : borrowDocumentDAO.getAll();
        totalPagesBorrowedBook = (int) Math.ceil((double) sourceList.size() / limit);
        int startIndex = (currentPageBorrowedBook - 1) * limit;
        int endIndex = Math.min(startIndex + limit, sourceList.size());
        lbl_page_info_borrow.setText("Page " + currentPageBorrowedBook + " of " + totalPagesBorrowedBook);
        updatePaginationBookButtons();

        Task<ObservableList<BorrowDocument>> loadPaginatedBorrowedBooksTask = new Task<>() {
            @Override
            protected ObservableList<BorrowDocument> call() throws Exception {
                return FXCollections.observableArrayList(sourceList.subList(startIndex, endIndex));
            }
        };
        loadPaginatedBorrowedBooksTask.setOnSucceeded(_ -> {
            tv_borrowDocument.setItems(loadPaginatedBorrowedBooksTask.getValue());
        });
        ImportDataToTableViewService.loadDataToTableView(tv_borrowDocument, loadPaginatedBorrowedBooksTask);
    }

    private void updateTableViewUser() {
        List<User> sourceList = isSearchingUser ? searchResultsUser : userDAO.getAll();
        totalPagesUser = (int) Math.ceil((double) sourceList.size() / limit);
        int startIndex = (currentPageUser - 1) * limit;
        int endIndex = Math.min(startIndex + limit, sourceList.size());
        lbl_page_info_user.setText("Page " + currentPageUser + " of " + totalPagesUser);
        updatePaginationBookButtons();

        Task<ObservableList<User>> loadPaginatedUserTask = new Task<>() {
            @Override
            protected ObservableList<User> call() throws Exception {
                return FXCollections.observableArrayList(sourceList.subList(startIndex, endIndex));
            }
        };
        loadPaginatedUserTask.setOnSucceeded(_ -> {
            tv_user.setItems(loadPaginatedUserTask.getValue());
        });
        ImportDataToTableViewService.loadDataToTableView(tv_user, loadPaginatedUserTask);
    }

    private void updatePaginationBookButtons() {
        btn_previous_book.setDisable(currentPageBook == 1);
        btn_next_book.setDisable(currentPageBook >= totalPagesBook);
    }

    private void updatePaginationBorrowBookButtons() {
        btn_previous_borrow.setDisable(currentPageBorrowedBook == 1);
        btn_next_borrow.setDisable(currentPageBorrowedBook >= totalPagesBorrowedBook);
    }

    private void updatePaginationUserButtons() {
        btn_previous_borrow.setDisable(currentPageUser == 1);
        btn_next_borrow.setDisable(currentPageUser >= totalPagesUser);
    }
}

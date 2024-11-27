package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Model.Annotation.RolePermissionRequired;
import com.hunghq.librarymanagement.Model.Entity.BorrowDocument;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Model.Entity.User;
import com.hunghq.librarymanagement.Respository.BorrowDocumentDAO;
import com.hunghq.librarymanagement.Respository.DocumentDAO;
import com.hunghq.librarymanagement.Respository.UserDAO;
import com.hunghq.librarymanagement.Service.FilterGenreService;

import com.hunghq.librarymanagement.Service.ImportDataToTableViewService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
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
    @RolePermissionRequired(roles = {"Librarian"})
    public Button btn_add_book;
    @FXML
    @RolePermissionRequired(roles = {"Librarian"})
    public Button btn_delete_book;
    @FXML
    @RolePermissionRequired(roles = {"Librarian"})
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


    @FXML private TableColumn<Document, String> col_book_id;
    @FXML private TableColumn<Document, String> col_title;
    @FXML private TableColumn<Document, String> col_author;
    @FXML private TableColumn<Document, String> col_isbn;
    @FXML private TableColumn<Document, String> col_genre;
    @FXML private TableColumn<Document, Integer> col_quantity;
    @FXML private TableColumn<Document, String> col_publisher;
    @FXML private TableColumn<Document, String> col_published_date;
    @FXML private TableView<Document> tv_book;


    @FXML private TableColumn<BorrowDocument, String> col_borrow_id;
    @FXML private TableColumn<BorrowDocument, String> col_document_id;
    @FXML private TableColumn<BorrowDocument, String> col_user_id;
    @FXML private TableColumn<BorrowDocument, Double> col_fee;
    @FXML private TableColumn<BorrowDocument, String> col_borrow_date;
    @FXML private TableColumn<BorrowDocument, String> col_due_date;
    @FXML private TableColumn<BorrowDocument, String> col_return_date;
    @FXML private TableColumn<BorrowDocument, String> col_extend_date;
    @FXML private TableColumn<BorrowDocument, String> col_state;
    @FXML private TableView<BorrowDocument> tv_borrowDocument;

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
    @RolePermissionRequired(roles = {"Librarian"})
    public Button btn_send_noti;
    @FXML
    @RolePermissionRequired(roles = {"Librarian"})
    public Button btn_return_book;
    @FXML
    public TextField tf_search_user;
    @FXML
    public Button btn_search_user;

    private FilterGenreService filterGenreService = new FilterGenreService();
    private DocumentDAO documentDAO = new DocumentDAO();
    private BorrowDocumentDAO borrowDocumentDAO = new BorrowDocumentDAO();
    private UserDAO userDAO = new UserDAO();

    @FXML
    @RolePermissionRequired(roles = {"Librarian"})
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        initializeBooksByGenreChart();
        initializeBorrowStatusPieChart();
        initializeBorrowingTrendsLineChart();
        initializeTableViewBook();
        initializeTableViewBorrowBook();
        initializeTotalDataView();
        initializeTableViewUser();

        btn_add_book.setOnAction(event -> handleAddBook());
        btn_delete_book.setOnAction(event -> handleDeleteBook());
        btn_edit_book.setOnAction(event -> handleEditBook());
        btn_search_book.setOnAction(event -> handleSearchBook());

        btn_send_noti.setOnAction(event -> handleSendNotification());
        btn_return_book.setOnAction(event -> handleReturnBook());
        btn_search_user.setOnAction(event -> handleSearchUser());
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
    }

    public void initializeBorrowingTrendsLineChart() {
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

        Task<ObservableList<Document>> loadBookDataTask = new Task<>() {
            @Override
            protected ObservableList<Document> call() throws Exception {
                return FXCollections.observableArrayList(documentDAO.getAll());
            }
        };
        ImportDataToTableViewService.loadDataToTableView(tv_book, loadBookDataTask);
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

        Task<ObservableList<BorrowDocument>> loadBorrowDataTask = new Task<>() {
            @Override
            protected ObservableList<BorrowDocument> call() throws Exception {
                return FXCollections.observableArrayList(borrowDocumentDAO.getAll());
            }
        };
        ImportDataToTableViewService.loadDataToTableView(tv_borrowDocument, loadBorrowDataTask);
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

        Task<ObservableList<User>> loadUserDataTask = new Task<>() {
            @Override
            protected ObservableList<User> call() throws Exception {
                return FXCollections.observableArrayList(userDAO.getAll());
            }
        };

        ImportDataToTableViewService.loadDataToTableView(tv_user, loadUserDataTask);
    }

    public void handleAddBook() {

    }

    public void handleDeleteBook() {

    }

    public void handleEditBook() {

    }

    public void handleSearchBook() {

    }

    public void handleSendNotification() {
    }

    public void handleReturnBook() {
    }

    public void handleSearchUser() {
    }

}

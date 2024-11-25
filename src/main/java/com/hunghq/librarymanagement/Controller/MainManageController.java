package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Model.Annotation.RolePermissionRequired;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Respository.DocumentDAO;
import com.hunghq.librarymanagement.Service.FilterGenreService;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainManageController extends BaseController {
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
    public TableView tv_book;
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

    @FXML
    @RolePermissionRequired(roles = {"Librarian"})
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        initializeBooksByGenreChart();
        initializeBorrowStatusPieChart();
        initializeBorrowingTrendsLineChart();
        initializeTableViewBook();
        initializeTableViewBorrowBook();

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
    }

    public void initializeTableViewBorrowBook() {
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

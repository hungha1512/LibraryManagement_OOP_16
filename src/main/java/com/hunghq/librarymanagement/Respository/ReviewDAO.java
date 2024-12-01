package com.hunghq.librarymanagement.Respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Model.Entity.Review;
import com.hunghq.librarymanagement.Model.Entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@SuppressWarnings("rawtypes")
public class ReviewDAO implements IRepository<Review>{

    private static final Connection con = MySQLConnection.getConnection();
    @Override
    public Review make(ResultSet reS) {
        Review review = null;
        
        try {
            DocumentDAO documentDAO = new DocumentDAO();
            Document document = (Document) documentDAO.getByStringId(reS.getString("documentId"));

            UserDAO userDAO = new UserDAO();
            User user = (User) userDAO.getByStringId(reS.getString("userId"));

            review = new Review(
                reS.getInt("reviewId"),
                document,
                user,
                reS.getDouble("rating"),
                reS.getString("reviewText"),
                reS.getTimestamp("reviewDate").toLocalDateTime()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return review;
    }

    @Override
    public boolean add(Review entity) {
        Review review = entity;
        String sql = "INSERT INTO reviews (reviewId, documentId, userId, "
                + "rating, reviewText, reviewDate) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setInt(1, review.getReviewId());
            prS.setString(2, review.getDocument().getDocumentId());
            prS.setInt(3, review.getUser().getUserId());
            prS.setDouble(4, review.getRating());
            prS.setString(5, review.getReviewText());
            prS.setTimestamp(6, Timestamp.valueOf(review.getReviewDate()));

            int rowsInserted = prS.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Review getByStringId(String id) {
        return null;
    }

    @Override
    public Review getByIntId(int id) {
        String sql = "SELECT * FROM reviews WHERE reviewId = ?";
        Review review = null;

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, id);
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                review = make(reS);
            } else {
                System.out.println("No review found with id: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return review;
    }

    @Override
    public ObservableList<Review> getAll() {
        String sql = "SELECT * FROM reviews";
        ObservableList<Review> reviews = FXCollections.observableArrayList();
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                reviews.add(make(reS));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reviews;
    }

    @Override
    public ObservableList<Review> findByName(String name) {
        String sql = "SELECT * FROM reviews r " 
                   + "JOIN documents d ON r.documentId = d.documentId " 
                   + "WHERE d.title LIKE ?";
        ObservableList<Review> reviews = FXCollections.observableArrayList();
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + name + "%");
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                reviews.add(make(reS));
            }
            if (reviews.isEmpty()) {
                System.out.println("No review found with document title: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reviews;
    }

    @Override
    public boolean update(Review entity) {
        String sql = "UPDATE reviews SET documentId = ?, userId = ?, rating = ?, " +
                "reviewText = ?, reviewDate = ? WHERE reviewId = ?";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            Review review = (Review) entity;

            // Gán giá trị vào PreparedStatement
            prS.setString(1, review.getDocument().getDocumentId());
            prS.setInt(2, review.getUser().getUserId());
            prS.setDouble(3, review.getRating());
            prS.setString(4, review.getReviewText());
            prS.setTimestamp(5, Timestamp.valueOf(review.getReviewDate()));
            prS.setInt(6, review.getReviewId());

            // Thực hiện câu lệnh cập nhật và kiểm tra số dòng bị ảnh hưởng
            int rowsAffected = prS.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất một dòng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu xảy ra lỗi
        }
    }


    @Override
    public void delete(String id) {
        String sql = "DELETE FROM reviews WHERE reviewId = ?";
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, Integer.parseInt(id));

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Review save(Review entity) {
        Review review = (Review) entity;
        
        if (getByIntId(review.getReviewId()) != null) {
            update(review);
        } else {
            add(review);
        }
        
        return review;
    }
    
}

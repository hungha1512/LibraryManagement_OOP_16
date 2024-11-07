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

@SuppressWarnings("rawtypes")
public class ReviewDAO implements IRepository{

    private static final Connection con = MySQLConnection.getConnection();
    @Override
    public Review make(ResultSet reS) {
        Review review = null;
        
        try {
            DocumentDAO documentDAO = new DocumentDAO();
            Document document = (Document) documentDAO.getById(reS.getString("documentId"));

            UserDAO userDAO = new UserDAO();
            User user = (User) userDAO.getById(reS.getString("userId"));

            review = new Review(
                reS.getString("reviewId"),
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
    public void add(Object entity) {
        Review review = (Review) entity;
        String sql = "INSERT INTO reviews (reviewId, documentId, userId, "
                   + "rating, reviewText, reviewDate) " 
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setString(1, review.getReviewId());
            prS.setString(2, review.getDocument().getDocumentId());
            prS.setString(3, review.getUser().getUserId());
            prS.setDouble(4, review.getRating());
            prS.setString(5, review.getReviewText());
            prS.setTimestamp(6, Timestamp.valueOf(review.getReviewDate()));
            
            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getById(String id) {
        String sql = "SELECT * FROM reviews WHERE reviewId = ?";
        Review review = null;
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, id);
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
    public List<Review> getAll() {
        String sql = "SELECT * FROM reviews";
        List<Review> reviews = new ArrayList<>();
        
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
    public List<Review> findByName(String name) {
        String sql = "SELECT * FROM reviews r " 
                   + "JOIN documents d ON r.documentId = d.documentId " 
                   + "WHERE d.title LIKE ?";
        List<Review> reviews = new ArrayList<>();
        
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
    public void update(Object entity) {
        Review review = (Review) entity;
        String sql = "UPDATE reviews SET documentId = ?, userId = ?, rating = ?, "
                   + "reviewText = ?, reviewDate = ? WHERE reviewId = ?";
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, review.getDocument().getDocumentId());
            prS.setString(2, review.getUser().getUserId());
            prS.setDouble(3, review.getRating());
            prS.setString(4, review.getReviewText());
            prS.setTimestamp(5, Timestamp.valueOf(review.getReviewDate()));
            prS.setString(6, review.getReviewId());
            
            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM reviews WHERE reviewId = ?";
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, id);

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object save(Object entity) {
        Review review = (Review) entity;
        
        if (getById(review.getReviewId()) != null) {
            update(review);
        } else {
            add(review);
        }
        
        return review;
    }
    
}

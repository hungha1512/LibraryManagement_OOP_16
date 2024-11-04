package com.hunghq.librarymanagement.Respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.Document;

@SuppressWarnings("rawtypes")
public class DocumentDAO implements IRepository{

    private static final Connection con = MySQLConnection.getConnection();
    
    @Override
    public Document Make(ResultSet reS) {
        Document document = null;
        try {
            document = new Document(
                reS.getString("documentId"), 
                reS.getString("title"),
                reS.getString("author"),
                reS.getDouble("rating"),
                reS.getString("genre"),
                reS.getString("language"),
                reS.getString("description"),
                reS.getInt("numRatings"),
                reS.getString("publisher"),
                reS.getString("isbn"),
                reS.getString("publishedDate"),
                reS.getString("award"),
                reS.getString("coverImg")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return document;
    }

    @Override
    public void Add(Object entity) {
        Document document = (Document) entity;
        String sql = "INSERT INTO documents (documentId, title, author, rating, genre, language, description, numRatings, publisher, isbn, publishedDate, award, coverImg) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setString(1, document.getDocumentId());
            prS.setString(2, document.getTitle());
            prS.setString(3, document.getAuthorName());
            prS.setDouble(4, document.getRating());
            prS.setString(5, document.getGenre());
            prS.setString(6, document.getLanguage());
            prS.setString(7, document.getDescription());
            prS.setInt(8, document.getNumRatings());
            prS.setString(9, document.getPublisher());
            prS.setString(10, document.getIsbn());
            prS.setString(11, document.getPublishedDate());
            prS.setString(12, document.getAward());
            prS.setString(13, document.getCoverImg());

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object GetById(String id) {
        String sql = "SELECT * FROM documents WHERE documentId = ?";
        Document document = null; 
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, id);
            ResultSet reS = prS.executeQuery();
            if (reS.next()) {
                document = Make(reS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return document; 
    }

    public List<Document> GetAll() {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents";
        try (PreparedStatement prS = con.prepareStatement(sql);
            ResultSet reS = prS.executeQuery()) {
            while (reS.next()) {
                documents.add(Make(reS));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    @Override
    public List<Document> FindByName(String name) {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE title LIKE ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + name + "%");
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                documents.add(Make(reS));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    @Override
    public void Update(Object entity) {
        Document document = (Document) entity;
        String sql = "UPDATE documents SET title = ?, author = ?, rating = ?, genre = ?, language = ?, description = ?, numRatings = ?, publisher = ?, isbn = ?, publishedDate = ?, award = ?, coverImg = ? WHERE documentId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setString(1, document.getTitle());
            prS.setString(2, document.getAuthorName());
            prS.setDouble(3, document.getRating());
            prS.setString(4, document.getGenre());
            prS.setString(5, document.getLanguage());
            prS.setString(6, document.getDescription());
            prS.setInt(7, document.getNumRatings());
            prS.setString(8, document.getPublisher());
            prS.setString(9, document.getIsbn());
            prS.setString(10, document.getPublishedDate());
            prS.setString(11, document.getAward());
            prS.setString(12, document.getCoverImg());
            prS.setString(13, document.getDocumentId()); 

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Delete(int id) {
        String sql = "DELETE FROM documents WHERE documentId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, id);

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object Save(Object entity) {
        Document document = (Document) entity;
        if (GetById(document.getDocumentId()) != null) {
            Update(document);
        } else {
            Add(document);
        }
        return document;
    }

}

package com.hunghq.librarymanagement.Respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.Document;

public class DocumentDAO implements IRepository{

    private static final Connection con = MySQLConnection.getConnection();
    
    @Override
    public Document Make(ResultSet reS) {
        Document document = null;
        try {
            if (reS.next()) { 
                document = new Document(
                    reS.getString("documentId"), 
                    reS.getString("title"),
                    reS.getString("authorName"),
                    reS.getDouble("rating"),
                    reS.getString("genre"),
                    reS.getString("language"),
                    reS.getString("description"),
                    reS.getInt("voterAmount"),
                    reS.getString("publisher"),
                    reS.getString("isbn"),
                    reS.getTimestamp("publishedDate").toLocalDateTime(),
                    reS.getString("award"),
                    reS.getString("coverImg")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return document;
    }

    @Override
    public void Add(Object entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Add'");
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

    public List GetAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetAll'");
    }

    @Override
    public List FindByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'FindByName'");
    }

    @Override
    public void Update(Object entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Update'");
    }

    @Override
    public void Delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Delete'");
    }

    @Override
    public Object Save(Object entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Save'");
    }

    public static void main(String[] args) {
        DocumentDAO documentDAO = new DocumentDAO();

        // Kiểm tra với một documentId cụ thể, ví dụ "D001"
        String documentId = "D001"; // Thay đổi ID này phù hợp với dữ liệu trong cơ sở dữ liệu
        Document document = (Document) documentDAO.GetById(documentId);
        
        if (document != null) {
            System.out.println("Document found:");
            System.out.println("Document ID: " + document.getDocumentId());
            System.out.println("Title: " + document.getTitle());
            System.out.println("Author: " + document.getAuthorName());
            System.out.println("Rating: " + document.getRating());
            System.out.println("Genre: " + document.getGenre());
            System.out.println("Language: " + document.getLanguage());
            System.out.println("Description: " + document.getDescription());
            System.out.println("Voter Amount: " + document.getVoterAmount());
            System.out.println("Publisher: " + document.getPublisher());
            System.out.println("ISBN: " + document.getIsbn());
            System.out.println("Published Date: " + document.getPublishedDate());
            System.out.println("Award: " + document.getAward());
            System.out.println("Cover Image: " + document.getCoverImg());
        } else {
            System.out.println("No document found with ID: " + documentId);
        }
    }

}

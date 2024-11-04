package com.hunghq.librarymanagement.Respository;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.Enum.State;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.BorrowDocument;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Model.Entity.User;

@SuppressWarnings("rawtypes")
public class BorrowDocumentDAO implements IRepository{

    private static final Connection con = MySQLConnection.getConnection();

    @Override
    public BorrowDocument Make(ResultSet reS) {
        try {
            DocumentDAO documentDAO = new DocumentDAO();
            Document document = (Document) documentDAO.GetById(reS.getString("documentId"));
    
            UserDAO userDAO = new UserDAO();
            User user = (User) userDAO.GetById(reS.getString("userId"));
    
            return new BorrowDocument(
                reS.getString("borrowId"),
                document,
                user,
                reS.getTimestamp("borrowDate").toLocalDateTime(),
                reS.getTimestamp("dueDate").toLocalDateTime(),
                reS.getTimestamp("returnDate").toLocalDateTime(),
                State.fromValue(reS.getString("state"))
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void Add(Object entity) {
        BorrowDocument borrowDocument = (BorrowDocument) entity;
        
        String sql = "INSERT INTO borrowDocuments (borrowId, documentId, userId, borrowDate, dueDate, returnDate, state) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
        
            prS.setString(1, borrowDocument.getBorrowId()); 
            prS.setString(2, borrowDocument.getDocument().getDocumentId());
            prS.setString(3, borrowDocument.getUser().getUserId()); 
            prS.setTimestamp(4, Timestamp.valueOf(borrowDocument.getBorrowDate())); 
            prS.setTimestamp(5, Timestamp.valueOf(borrowDocument.getDueDate()));
            prS.setTimestamp(6, Timestamp.valueOf(borrowDocument.getReturnDate()));
            prS.setString(7, borrowDocument.getState().getState()); 
            
            prS.executeUpdate(); 
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }

    @Override
    public BorrowDocument GetById(String borrowId) {
        String sql = "SELECT * FROM borrowDocuments WHERE borrowId = ?";
        BorrowDocument borrowDocument = null;
        try (PreparedStatement prS = con.prepareStatement(sql)){
            prS.setString(1, borrowId);
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                borrowDocument = Make(reS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowDocument;
    }

    @Override
    public List<BorrowDocument> GetAll() {
        List<BorrowDocument> borrowDocuments = new ArrayList<>();
        String sql = "SELECT * FROM borrowDocuments"; 
        try (Statement stM = con.createStatement();
            ResultSet reS = stM.executeQuery(sql)) {
            while (reS.next()) {
                BorrowDocument borrowDocument = Make(reS);

                if (borrowDocument != null) {
                    borrowDocuments.add(borrowDocument);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowDocuments;
    }

    @Override
    public List<BorrowDocument> FindByName(String name) {
            List<BorrowDocument> borrowDocuments = new ArrayList<>();
            String sql = "SELECT bd.*, d.*, u.* FROM borrowDocuments bd " +
                         "JOIN documents d ON bd.documentId = d.documentId " +
                         "JOIN users u ON bd.userId = u.userId " +
                         "WHERE d.title LIKE ?"; 
        
            try (PreparedStatement prT = con.prepareStatement(sql)) {
                prT.setString(1, "%" + name + "%"); 
        
                try (ResultSet reS = prT.executeQuery()) {
                    while (reS.next()) {
                        
                        BorrowDocument borrowDocument = Make(reS);
                        if (borrowDocument != null) {
                            borrowDocuments.add(borrowDocument);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return borrowDocuments;
        }
    
    @Override
    public void Update(Object entity) {
        BorrowDocument borrowDocument = (BorrowDocument) entity;
        String sql = "UPDATE borrowDocuments SET documentId = ?, userId = ?, borrowDate = ?, dueDate = ?, returnDate = ?, state = ? WHERE borrowId = ?";
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
                
            prS.setString(1, borrowDocument.getDocument().getDocumentId());
            prS.setString(2, borrowDocument.getUser().getUserId());
            prS.setTimestamp(3, Timestamp.valueOf(borrowDocument.getBorrowDate()));
            prS.setTimestamp(4, Timestamp.valueOf(borrowDocument.getDueDate()));
            prS.setTimestamp(5, Timestamp.valueOf(borrowDocument.getReturnDate()));
            prS.setString(6, borrowDocument.getState().getState()); 
            prS.setString(7, borrowDocument.getBorrowId()); 
        
            prS.executeUpdate();
                
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }
    
    @Override
    public void Delete(int id) {
        String sql = "DELETE FROM borrowDocuments WHERE borrowId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, id);
            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Object Save(Object entity) {
        BorrowDocument borrowDocument = (BorrowDocument) entity; 
        if (GetById(borrowDocument.getBorrowId()) != null) {
            Update(borrowDocument);
        } else {
            Add(borrowDocument);
        }
        return borrowDocument;
    }

}

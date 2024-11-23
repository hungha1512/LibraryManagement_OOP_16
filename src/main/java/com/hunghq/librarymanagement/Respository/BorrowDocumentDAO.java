package com.hunghq.librarymanagement.Respository;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.BorrowDocument;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Model.Entity.User;
import com.hunghq.librarymanagement.Model.Enum.EState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BorrowDocumentDAO provides CRUD operations and specific queries
 * for BorrowDocument objects within a library management system.
 */
@SuppressWarnings("rawtypes")
public class BorrowDocumentDAO implements IRepository<BorrowDocument> {

    private static final Connection con = MySQLConnection.getConnection();

    /**
     * Constructs a BorrowDocument object from the provided ResultSet.
     *
     * @param reS the ResultSet containing BorrowDocument data
     * @return a BorrowDocument object constructed from the ResultSet, or null if an error occurs
     */
    @Override
    public BorrowDocument make(ResultSet reS) {
        try {
            DocumentDAO documentDAO = new DocumentDAO();
            Document document = (Document) documentDAO.getByStringId(reS.getString("documentId"));

            UserDAO userDAO = new UserDAO();
            User user = (User) userDAO.getByStringId(reS.getString("userId"));

            return new BorrowDocument(
                    reS.getInt("borrowId"),
                    document,
                    user,
                    reS.getTimestamp("borrowDate") != null
                            ? reS.getTimestamp("borrowDate").toLocalDateTime() : null,
                    reS.getTimestamp("dueDate") != null
                            ? reS.getTimestamp("dueDate").toLocalDateTime() : null,
                    reS.getTimestamp("returnDate") != null
                            ? reS.getTimestamp("returnDate").toLocalDateTime() : null,
                    EState.fromValue(reS.getString("state"))
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a new BorrowDocument record to the database.
     *
     * @param entity the BorrowDocument object to be added
     */
    @Override
    public void add(BorrowDocument entity) {
        BorrowDocument borrowDocument = (BorrowDocument) entity;

        String sql = "INSERT INTO borrowDocuments (borrowId, documentId, userId, borrowDate, dueDate, returnDate, state) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setInt(1, borrowDocument.getBorrowId());
            prS.setString(2, borrowDocument.getDocument().getDocumentId());
            prS.setInt(3, borrowDocument.getUser().getUserId());
            prS.setTimestamp(4, Timestamp.valueOf(borrowDocument.getBorrowDate()));
            prS.setTimestamp(5, Timestamp.valueOf(borrowDocument.getDueDate()));
            prS.setTimestamp(6, borrowDocument.getReturnDate() != null
                    ? Timestamp.valueOf(borrowDocument.getReturnDate())
                    : null);
            prS.setString(7, borrowDocument.getState().getState());

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a BorrowDocument by its unique borrowId.
     *
     * @param borrowId the unique ID of the BorrowDocument
     * @return the BorrowDocument with the specified borrowId, or null if not found
     */
    @Override
    public BorrowDocument getByStringId(String borrowId) {
        return null;
    }

    /**
     * Retrieves a BorrowDocument by its integer ID (not implemented).
     *
     * @param id the integer ID
     * @return null, as this method is not implemented
     */
    @Override
    public BorrowDocument getByIntId(int id) {
        String sql = "SELECT * FROM borrowDocuments WHERE borrowId = ?";
        BorrowDocument borrowDocument = null;
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, id);
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                borrowDocument = make(reS);
            } else {
                System.out.println("No BorrowDocument found with borrowId: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowDocument;
    }

    /**
     * Retrieves all BorrowDocument records from the database.
     *
     * @return a list of all BorrowDocument objects in the database
     */
    @Override
    public ObservableList<BorrowDocument> getAll() {
        ObservableList<BorrowDocument> borrowDocuments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM borrowDocuments";
        try (Statement stM = con.createStatement();
             ResultSet reS = stM.executeQuery(sql)) {
            while (reS.next()) {
                BorrowDocument borrowDocument = make(reS);
                borrowDocuments.add(borrowDocument);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowDocuments;
    }

    /**
     * Searches for BorrowDocument records by document title.
     *
     * @param name the title of the document to search for
     * @return a list of BorrowDocument objects with documents that match the title
     */
    @Override
    public ObservableList<BorrowDocument> findByName(String name) {
        ObservableList<BorrowDocument> borrowDocuments = FXCollections.observableArrayList();
        String sql = "SELECT bd.*, d.*, u.* FROM borrowDocuments bd " +
                "JOIN documents d ON bd.documentId = d.documentId " +
                "JOIN users u ON bd.userId = u.userId " +
                "WHERE d.title LIKE ?";

        try (PreparedStatement prT = con.prepareStatement(sql)) {
            prT.setString(1, "%" + name + "%");

            try (ResultSet reS = prT.executeQuery()) {
                while (reS.next()) {
                    BorrowDocument borrowDocument = make(reS);
                    borrowDocuments.add(borrowDocument);
                }
                if (borrowDocuments.isEmpty()) {
                    System.out.println("No BorrowDocument found with name: " + name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowDocuments;
    }

    /**
     * Updates an existing BorrowDocument record in the database.
     *
     * @param entity the BorrowDocument object with updated information
     */
    @Override
    public void update(BorrowDocument entity) {
        BorrowDocument borrowDocument = (BorrowDocument) entity;
        String sql = "UPDATE borrowDocuments SET documentId = ?, userId = ?, borrowDate = ?, dueDate = ?, returnDate = ?, state = ? WHERE borrowId = ?";

        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setString(1, borrowDocument.getDocument().getDocumentId());
            prS.setInt(2, borrowDocument.getUser().getUserId());
            prS.setTimestamp(3, Timestamp.valueOf(borrowDocument.getBorrowDate()));
            prS.setTimestamp(4, Timestamp.valueOf(borrowDocument.getDueDate()));
            prS.setTimestamp(6, borrowDocument.getReturnDate() != null
                    ? Timestamp.valueOf(borrowDocument.getReturnDate())
                    : null);
            prS.setString(6, borrowDocument.getState().getState());
            prS.setInt(7, borrowDocument.getBorrowId());

            prS.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a BorrowDocument record from the database using its borrowId.
     *
     * @param id the unique ID of the BorrowDocument to delete
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM borrowDocuments WHERE borrowId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, Integer.parseInt(id));
            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a BorrowDocument record in the database. If the record already exists,
     * it updates the record; otherwise, it adds a new record.
     *
     * @param entity the BorrowDocument object to save
     * @return the saved BorrowDocument object
     */
    @Override
    public BorrowDocument save(BorrowDocument entity) {
        BorrowDocument borrowDocument = (BorrowDocument) entity;
        if (getByIntId(borrowDocument.getBorrowId()) != null) {
            update(borrowDocument);
        } else {
            add(borrowDocument);
        }
        return borrowDocument;
    }

    /**
     *
     * @param userId
     * @return
     */
    public ObservableList<BorrowDocument> getBorrowDocumentByUserId(int userId) {
        ObservableList<BorrowDocument> borrowDocuments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM borrowDocuments WHERE userId = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BorrowDocument borrowDocument = make(rs);
                    borrowDocuments.add(borrowDocument);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowDocuments;
    }
}

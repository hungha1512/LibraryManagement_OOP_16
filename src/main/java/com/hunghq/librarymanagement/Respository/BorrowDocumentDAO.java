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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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
            User user = userDAO.getByIntId(reS.getInt("userId"));

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
                    reS.getTimestamp("extendDate") != null
                            ? reS.getTimestamp("extendDate").toLocalDateTime() : null,
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

        String sql = "INSERT INTO borrowDocuments (documentId, userId, borrowDate, dueDate, returnDate, state) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement prS = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Thiết lập các giá trị cho câu lệnh SQL
            prS.setString(1, borrowDocument.getDocument().getDocumentId()); // Document ID (String)
            prS.setInt(2, borrowDocument.getUser().getUserId());            // User ID (Integer)
            prS.setTimestamp(3, Timestamp.valueOf(borrowDocument.getBorrowDate())); // Borrow Date
            prS.setTimestamp(4, Timestamp.valueOf(borrowDocument.getDueDate()));    // Due Date
            prS.setTimestamp(5, borrowDocument.getReturnDate() != null
                    ? Timestamp.valueOf(borrowDocument.getReturnDate())    // Return Date nếu có
                    : null);
            prS.setString(6, borrowDocument.getState().getState());        // State

            // Thực thi câu lệnh
            prS.executeUpdate();

            // Lấy giá trị ID tự động tăng (borrowId)
            try (ResultSet generatedKeys = prS.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Gán giá trị borrowId cho đối tượng BorrowDocument
                    borrowDocument.setBorrowId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding BorrowDocument", e);
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
        String sql = "SELECT * FROM borrowdocuments WHERE documentId = ?";
        BorrowDocument borrowDocument = null;
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, borrowId);
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                borrowDocument = make(reS);
            } else {
                System.out.println("No document found with id: " + borrowId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowDocument;
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
        String sql = "UPDATE borrowDocuments SET documentId = ?, userId = ?, borrowDate = ?, " +
                "dueDate = ?, returnDate = ?, state = ? WHERE borrowId = ?";

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


    public ObservableList<BorrowDocument> getBorrowDocumentByUserId(int userId) {
        ObservableList<BorrowDocument> borrowDocuments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM borrowDocuments WHERE userId = ? AND state != 'Returned'";
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


    public boolean isDocumentBorrowed(String documentId, int userId) {
        String sql = "SELECT COUNT(*) FROM borrowDocuments WHERE documentId = ? AND userId = ? AND state = 'Borrowed'";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, documentId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isDocumentOverdue(String documentId, int userId) {
        String sql = "SELECT COUNT(*) FROM borrowDocuments WHERE documentId = ? AND userId = ? AND state = 'Overdue'";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, documentId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public LocalDateTime getLatestReturnDateTime() {
        String sql = "SELECT returnDate FROM borrowDocuments WHERE state = 'Returned' ORDER BY returnDate DESC LIMIT 1";
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("returnDate");
                if (timestamp != null) {
                    return timestamp.toLocalDateTime();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public LocalDateTime getBorrowDate(String documentId, int userId) {
        String sql = "SELECT borrowDate FROM borrowDocuments WHERE documentId = ? AND userId = ? AND state = 'Borrowed'";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, documentId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("borrowDate").toLocalDateTime();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LocalDateTime getExtendDate(String documentId, int userId) {
        String sql = "SELECT extendDate FROM borrowDocuments WHERE documentId = ? AND userId = ? AND state = 'Borrowed'";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, documentId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("extendDate");
                    LocalDateTime extendDate = timestamp != null ? timestamp.toLocalDateTime() : null;
                    return extendDate;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LocalDateTime getDueDate(String documentId, int userId) {
        String sql = "SELECT dueDate FROM borrowDocuments WHERE documentId = ? AND userId = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, documentId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("dueDate").toLocalDateTime();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBorrowDocumentStateToReturned(String documentId, int userId) {
        String sql = """
        UPDATE borrowDocuments bd
        JOIN (
            SELECT MAX(borrowDate) AS maxBorrowDate 
            FROM borrowDocuments 
            WHERE documentId = ? AND userId = ?
        ) subquery ON bd.borrowDate = subquery.maxBorrowDate
        SET bd.state = 'Returned', bd.returnDate = NOW()
        WHERE bd.documentId = ? AND bd.userId = ?
        """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, documentId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, documentId);  // documentId cho subquery
            pstmt.setInt(4, userId);         // userId cho subquery

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("The borrow document state has been successfully updated to Returned.");
            } else {
                System.out.println("No matching record found or the record is not in the 'Borrowed' state.");
            }
        } catch (SQLException e) {
            System.err.println("Error while updating borrow document state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean canBorrowAfterReturn(String documentId, int userId) {
        String sql = "SELECT returnDate FROM borrowdocuments WHERE documentId = ? " +
                "AND userId = ? AND state = 'Returned' ORDER BY returnDate DESC LIMIT 1";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, documentId);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Date returnDate = rs.getDate("returnDate");

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(returnDate);
                    calendar.add(Calendar.MINUTE, 1 );
                    Date threeMonthsLater = new Date(calendar.getTimeInMillis());

                    return new Date(System.currentTimeMillis()).after(threeMonthsLater);
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateDueDate(String documentId, int userId) {
        String sql = "UPDATE borrowdocuments SET dueDate = DATE_ADD(dueDate, INTERVAL 7 DAY) " +
                "WHERE documentId = ? AND userId = ? AND state = 'Borrowed'";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, documentId);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateExtendDate(String documentId, int userId) {
        String sql = "UPDATE borrowdocuments SET extendDate = CURRENT_TIMESTAMP WHERE" +
                " documentId = ? AND userId = ? AND state = 'Borrowed'";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, documentId);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean limitOverdueDocument(int userId) {
        boolean result = false;
        String sql = "SELECT COUNT(*) FROM borrowDocuments WHERE userId = ? AND state = 'Overdue'";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    result = count >= 3;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



}

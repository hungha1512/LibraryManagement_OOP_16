package com.hunghq.librarymanagement.Respository;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Model.Enum.EIsDeleted;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.print.Doc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object (DAO) for handling CRUD operations and queries
 * related to the Document entity in the library management system.
 */
@SuppressWarnings("rawtypes")
public class DocumentDAO implements IRepository<Document> {

    /**
     * Connection to the MySQL database.
     */
    private static final Connection con = MySQLConnection.getConnection();

    /**
     * Constructs a Document object from the provided ResultSet.
     *
     * @param reS the ResultSet containing document data
     * @return a Document object created from the ResultSet, or null if an error occurs
     */
    @Override
    public Document make(ResultSet reS) {
        Document document = null;
        try {
            document = new Document(
                    reS.getString("documentId"),
                    reS.getString("title"),
                    reS.getString("author"),
                    reS.getDouble("rating"),
                    reS.getString("description"),
                    reS.getString("language"),
                    reS.getString("isbn"),
                    reS.getString("genre"),
                    reS.getInt("quantity"),
                    reS.getString("publisher"),
                    reS.getString("publishedDate"),
                    reS.getString("award"),
                    reS.getInt("numRatings"),
                    reS.getString("coverImg")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * Adds a new document record to the database.
     *
     * @param entity the Document object to be added
     */
    @Override
    public boolean add(Document entity) {
        Document document = (Document) entity;
        String sql = "INSERT INTO documents (documentId, title, author, rating, description, "
                + "language, isbn, genre, quantity, "
                + "publisher, publishedDate, award, numRatings, coverImg) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setString(1, document.getDocumentId());
            prS.setString(2, document.getTitle());
            prS.setString(3, document.getAuthor());
            prS.setDouble(4, document.getRating());
            prS.setString(5, document.getDescription());
            prS.setString(6, document.getLanguage());
            prS.setString(7, document.getIsbn());
            prS.setString(8, document.getGenre());
            prS.setInt(9, document.getQuantity());
            prS.setString(10, document.getPublisher());
            prS.setString(11, document.getPublishedDate());
            prS.setString(12, document.getAward());
            prS.setInt(13, document.getNumRatings());
            prS.setString(14, document.getCoverImg());

            int rowsInserted = prS.executeUpdate();  // Kiểm tra số dòng đã thay đổi

            return rowsInserted > 0;  // Trả về true nếu có bản ghi được thêm, false nếu không

        } catch (SQLException e) {
            e.printStackTrace();  // In lỗi nếu có
            return false;  // Trả về false nếu có lỗi
        }
    }


    /**
     * Retrieves a Document by its unique document ID.
     *
     * @param id the unique document ID
     * @return the Document with the specified documentId, or null if not found
     */
    @Override
    public Document getByStringId(String id) {
        String sql = "SELECT * FROM documents WHERE documentId = ?";
        Document document = null;
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, id);
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                document = make(reS);
            } else {
                System.out.println("No document found with id: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * Retrieves a Document by its integer ID (not implemented).
     *
     * @param id the integer ID
     * @return null, as this method is not implemented
     */
    @Override
    public Document getByIntId(int id) {
        return null;
    }

    /**
     * Retrieves all Document records from the database.
     *
     * @return a list of all Document objects in the database
     */
    public ObservableList<Document> getAll() {
        ObservableList<Document> documents = FXCollections.observableArrayList();
        String sql = "SELECT * FROM documents";
        try (PreparedStatement prS = con.prepareStatement(sql);
             ResultSet reS = prS.executeQuery()) {

            while (reS.next()) {
                documents.add(make(reS));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    /**
     * Searches for documents by title.
     *
     * @param name the title or part of the title to search for
     * @return a list of Document objects that match the search term
     */
    @Override
    public ObservableList<Document> findByName(String name) {
        ObservableList<Document> documents = FXCollections.observableArrayList();
        String sql = "SELECT * FROM documents WHERE title LIKE ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + name + "%");
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                documents.add(make(reS));
            }
            if (documents.isEmpty()) {
                System.out.println("No document found with name: " + name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    /**
     * Updates an existing document record in the database.
     *
     * @param entity the Document object with updated information
     */
    @Override
    public boolean update(Document entity) {
        String sql = "UPDATE documents SET title = ?, author = ?, rating = ?, " +
                "description = ?, language = ?, isbn = ?, genre = ?, " +
                "quantity = ?, publisher = ?, publishedDate = ?, award = ?, " +
                "numRatings = ?, coverImg = ? WHERE documentId = ?";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            Document document = (Document) entity;

            // Gán giá trị vào PreparedStatement
            prS.setString(1, document.getTitle());
            prS.setString(2, document.getAuthor());
            prS.setDouble(3, document.getRating());
            prS.setString(4, document.getDescription());
            prS.setString(5, document.getLanguage());
            prS.setString(6, document.getIsbn());
            prS.setString(7, document.getGenre());
            prS.setInt(8, document.getQuantity());
            prS.setString(9, document.getPublisher());
            prS.setString(10, document.getPublishedDate());
            prS.setString(11, document.getAward());
            prS.setInt(12, document.getNumRatings());
            prS.setString(13, document.getCoverImg());
            prS.setString(14, document.getDocumentId());

            // Thực hiện cập nhật và kiểm tra số dòng bị ảnh hưởng
            int rowsAffected = prS.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất một dòng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu xảy ra lỗi
        }
    }


    /**
     * Deletes a document record from the database by its document ID.
     *
     * @param id the unique document ID of the document to delete
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM documents WHERE documentId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, id);

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a document record in the database. If the document already exists,
     * it updates the record; otherwise, it adds a new record.
     *
     * @param entity the Document object to save
     * @return the saved Document object
     */
    @Override
    public Document save(Document entity) {
        Document document = (Document) entity;
        if (getByStringId(document.getDocumentId()) != null) {
            update(document);
        } else {
            add(document);
        }
        return document;
    }

    /**
     * Main method for testing the DocumentDAO functionality, including
     * fetching documents by ID and by name.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        DocumentDAO documentDAO = new DocumentDAO();
        String testId = "UET00000001";
        Document documentById = (Document) documentDAO.getByStringId(testId);
        if (documentById != null) {
            System.out.println("Document found with ID " + testId + ":");
            System.out.println("Title: " + documentById.getTitle());
            System.out.println("Author: " + documentById.getAuthor());
            System.out.println("Rating: " + documentById.getRating());
        } else {
            System.out.println("No document found with ID: " + testId);
        }
        String testName = "The Hunger Game";
        List<Document> documentsByName = documentDAO.findByName(testName);

        if (!documentsByName.isEmpty()) {
            System.out.println("\nDocuments found with name containing \"" + testName + "\":");
            for (Document doc : documentsByName) {
                System.out.println("ID: " + doc.getDocumentId() + ", Title: " + doc.getTitle() + ", Author: " + doc.getAuthor());
            }
        } else {
            System.out.println("No document found with name containing: " + testName);
        }
    }

    /**
     * Retrieves all Document records from the database.
     *
     * @return a list of all Document objects in the database
     */
    public ObservableList<Document> getAllWithOffset(int offset) {
        ObservableList<Document> documents = FXCollections.observableArrayList();
        String sql = "SELECT * FROM documents LIMIT 15 OFFSET " + offset;
        try (PreparedStatement prS = con.prepareStatement(sql);
             ResultSet reS = prS.executeQuery()) {

            while (reS.next()) {
                documents.add(make(reS));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public ObservableList<Document> searchByISBN(String isbn) {
        ObservableList<Document> documents = FXCollections.observableArrayList();
        String sql = "SELECT * FROM documents WHERE isbn LIKE ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + isbn + "%");
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                documents.add(make(reS));
            }
            if (documents.isEmpty()) {
                System.out.println("No document found with isbn: " + isbn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public ObservableList<Document> searchByGenre(String genre) {
        ObservableList<Document> documents = FXCollections.observableArrayList();
        String sql = "SELECT * FROM documents WHERE genre LIKE ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + genre + "%");
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                documents.add(make(reS));
            }
            if (documents.isEmpty()) {
                System.out.println("No document found with name: " + genre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public ObservableList<Document> searchByAuthor(String author) {
        ObservableList<Document> documents = FXCollections.observableArrayList();
        String sql = "SELECT * FROM documents WHERE author LIKE ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + author + "%");
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                documents.add(make(reS));
            }
            if (documents.isEmpty()) {
                System.out.println("No document found with name: " + author);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public boolean updateBookQuantityWhenReturn(String documentId, int userId) {
        String sql = """
        UPDATE documents d
        JOIN borrowDocuments bd ON d.documentId = bd.documentId
        SET d.quantity = d.quantity + 1
        WHERE d.documentId = ? AND bd.userId = ? 
        AND bd.state = 'Returned'
        AND bd.borrowDate = (
            SELECT MAX(borrowDate) 
            FROM borrowDocuments 
            WHERE documentId = ? AND userId = ?
        ) AND d.quantity >= 0
    """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, documentId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, documentId); // documentId cho subquery
            pstmt.setInt(4, userId); // userId cho subquery

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBookQuantityWhenBorrow(String documentId, int userId) {
        String sql = """
        UPDATE documents d
        JOIN borrowDocuments bd ON d.documentId = bd.documentId
        SET d.quantity = d.quantity - 1
        WHERE d.documentId = ? AND bd.userId = ?
        AND bd.state = 'Borrowed'
        AND bd.borrowDate = (
            SELECT MAX(borrowDate) 
            FROM borrowDocuments 
            WHERE documentId = ? AND userId = ?
        ) AND d.quantity > 0
    """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, documentId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, documentId);
            pstmt.setInt(4, userId);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getDocumentQuantity(String documentId) {
        String sql = "SELECT quantity FROM documents WHERE documentId = ?";
        int quantity = 0;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, documentId);  // Set the documentId parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    quantity = rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quantity;
    }

    public ObservableList<Document> getDocumentsWithReturnedState() {
        ObservableList<Document> documents = FXCollections.observableArrayList();

        String query = """
        SELECT d.*
        FROM documents d
        JOIN borrowDocuments b ON d.documentId = b.documentId
        WHERE b.state = 'Returned';
    """;

        try (PreparedStatement preparedStatement = con.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // Sử dụng phương thức make để tạo Document
                Document document = make(resultSet);
                if (document != null) {
                    documents.add(document);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documents;
    }

    public String getMaxDocumentID() {
        String maxID = null;
        String query = "SELECT MAX(documentId) AS maxDocumentId FROM documents";

        try (PreparedStatement prS = con.prepareStatement(query);
         ResultSet reS = prS.executeQuery()) {
            if (reS.next()) {
                maxID = reS.getString("maxDocumentId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxID;
    }
}

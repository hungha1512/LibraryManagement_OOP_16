package com.hunghq.librarymanagement.Respository;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.Document;
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
                    reS.getString("genre"),
                    reS.getInt("quantity"),
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

    /**
     * Adds a new document record to the database.
     *
     * @param entity the Document object to be added
     */
    @Override
    public void add(Document entity) {
        Document document = (Document) entity;
        String sql = "INSERT INTO documents (documentId, title, author, rating, genre, " +
                "quantity, language, description, numRatings, " +
                "publisher, isbn, publishedDate, award, coverImg) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setString(1, document.getDocumentId());
            prS.setString(2, document.getTitle());
            prS.setString(3, document.getAuthorName());
            prS.setDouble(4, document.getRating());
            prS.setString(5, document.getGenre());
            prS.setInt(6, document.getQuantity());
            prS.setString(7, document.getLanguage());
            prS.setString(8, document.getDescription());
            prS.setInt(9, document.getNumRatings());
            prS.setString(10, document.getPublisher());
            prS.setString(11, document.getIsbn());
            prS.setString(12, document.getPublishedDate());
            prS.setString(13, document.getAward());
            prS.setString(14, document.getCoverImg());

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
    public void update(Document entity) {
        Document document = (Document) entity;
        String sql = "UPDATE documents SET title = ?, author = ?, rating = ?, " +
                "genre = ?, quantity = ?, language = ?, description = ?, " +
                "numRatings = ?, publisher = ?, isbn = ?, publishedDate = ?, " +
                "award = ?, coverImg = ? WHERE documentId = ?";
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
            System.out.println("Author: " + documentById.getAuthorName());
            System.out.println("Rating: " + documentById.getRating());
        } else {
            System.out.println("No document found with ID: " + testId);
        }
        String testName = "The Hunger Game";
        List<Document> documentsByName = documentDAO.findByName(testName);

        if (!documentsByName.isEmpty()) {
            System.out.println("\nDocuments found with name containing \"" + testName + "\":");
            for (Document doc : documentsByName) {
                System.out.println("ID: " + doc.getDocumentId() + ", Title: " + doc.getTitle() + ", Author: " + doc.getAuthorName());
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

    /**
     *
     * @param documentId
     * @param userId
     * @return
     */
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
            pstmt.setString(3, documentId); // documentId cho subquery
            pstmt.setInt(4, userId); // userId cho subquery

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


}

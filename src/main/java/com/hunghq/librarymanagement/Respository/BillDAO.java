package com.hunghq.librarymanagement.Respository;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.Bill;
import com.hunghq.librarymanagement.Model.Entity.BorrowDocument;
import com.hunghq.librarymanagement.Model.Entity.User;
import com.hunghq.librarymanagement.Model.Enum.EPaymentStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * BillDAO provides CRUD operations for Bill objects within the library management system.
 */
@SuppressWarnings("rawtypes")
public class BillDAO implements IRepository<Bill> {

    private static final Connection con = MySQLConnection.getConnection();

    /**
     * Constructs a Bill object from the provided ResultSet.
     *
     * @param reS the ResultSet containing Bill data
     * @return a Bill object constructed from the ResultSet, or null if an error occurs
     */
    @Override
    public Bill make(ResultSet reS) {
        try {
            BorrowDocumentDAO borrowDocumentDAO = new BorrowDocumentDAO();
            BorrowDocument borrowDocument = (BorrowDocument) borrowDocumentDAO.getByIntId(reS.getInt("borrowId"));

            UserDAO userDAO = new UserDAO();
            User user = (User) userDAO.getByIntId(reS.getInt("userId"));

            double totalPayment = borrowDocumentDAO.totalPayment(user.getUserId());

            return new Bill(
                    reS.getInt("billId"),
                    borrowDocument,
                    user,
                    totalPayment,
                    reS.getTimestamp("creationDate") != null
                            ? reS.getTimestamp("creationDate").toLocalDateTime() : null,
                    reS.getTimestamp("paymentDate") != null
                            ? reS.getTimestamp("paymentDate").toLocalDateTime() : null,
                    EPaymentStatus.fromValue(reS.getString("paymentStatus"))
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a new Bill record to the database.
     *
     * @param entity the Bill object to be added
     */
    @Override
    public void add(Bill entity) {
        String sql = "INSERT INTO `bills` (billId, borrowId, userId, totalPayment, " +
                "creationDate, paymentDate, paymentStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            Bill bill = (Bill) entity;

            prS.setInt(1, bill.getBillId());
            prS.setInt(2, bill.getBorrowDocument().getBorrowId());
            prS.setInt(3, bill.getUser().getUserId());
            prS.setDouble(4, bill.getTotalPayment());

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a Bill by its unique billId.
     *
     * @param billId the unique ID of the Bill
     * @return the Bill object, or null if not found
     */
    @Override
    public Bill getByStringId(String billId) {
        return null;
    }

    /**
     * Retrieves a Bill by its integer ID.
     *
     * @param id the integer ID of the Bill
     * @return the Bill object, or null if not found
     */
    @Override
    public Bill getByIntId(int id) {
        String sql = "SELECT * FROM bills WHERE billId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, id);
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                return make(reS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all Bill records from the database.
     *
     * @return a list of all Bill objects
     */
    @Override
    public ObservableList<Bill> getAll() {
        ObservableList<Bill> bills = FXCollections.observableArrayList();
        String sql = "SELECT * FROM bills";
        try (Statement stM = con.createStatement();
             ResultSet reS = stM.executeQuery(sql)) {
            while (reS.next()) {
                Bill bill = make(reS);
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    /**
     * Find bill by username.
     *
     * @param fullName username
     * @return bill
     */
    @Override
    public ObservableList<Bill> findByName(String fullName) {
        ObservableList<Bill> bills = FXCollections.observableArrayList();
        String sql = "SELECT b.* FROM bills b " +
                "JOIN users u ON b.userId = u.userId " +
                "WHERE u.fullName = ?";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, fullName);
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                Bill bill = make(reS);
                bills.add(bill);
            }

            if (bills.isEmpty()) {
                System.out.println("No Bill found for userName: " + fullName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bills;
    }


    /**
     * Updates an existing Bill record in the database.
     *
     * @param entity the Bill object with updated information
     */
    @Override
    public boolean update(Bill entity) {
        String sql = "UPDATE bills SET borrowId = ?, userId = ?, totalPayment = ?, " +
                "creationDate = ?, paymentDate = ?, paymentStatus = ? WHERE billId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            Bill bill = (Bill) entity;

            prS.setInt(1, bill.getBorrowDocument().getBorrowId());
            prS.setInt(2, bill.getUser().getUserId());
            prS.setDouble(3, bill.getTotalPayment());
            prS.setTimestamp(4, bill.getCreationDate() != null
                    ? Timestamp.valueOf(bill.getCreationDate())
                    : null);
            prS.setTimestamp(5, bill.getPaymentDate() != null
                    ? Timestamp.valueOf(bill.getPaymentDate())
                    : null);
            prS.setString(6, bill.getPaymentStatus().getStatus());
            prS.setInt(7, bill.getBillId());


            int rowsAffected = prS.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Deletes a Bill record from the database using its billId.
     *
     * @param id the unique ID of the Bill to delete
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM bills WHERE billId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, Integer.parseInt(id));
            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a Bill record in the database. If the record already exists, it updates it; otherwise, it adds a new one.
     *
     * @param entity the Bill object to save
     * @return the saved Bill object
     */
    @Override
    public Bill save(Bill entity) {
        Bill bill = entity;
        if (getByIntId(bill.getBillId()) != null) {
            update(bill);
        } else {
            add(bill);
        }
        return bill;
    }


}

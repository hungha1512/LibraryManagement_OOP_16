package com.hunghq.librarymanagement.Respository;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.Bill;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Model.Entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            // Fetch Document and User from their respective DAOs
            DocumentDAO documentDAO = new DocumentDAO();
            Document document = (Document) documentDAO.getByStringId(reS.getString("documentId"));

            UserDAO userDAO = new UserDAO();
            User user = (User) userDAO.getByStringId(reS.getString("userId"));

            return new Bill(
                    reS.getInt("billId"),
                    document,
                    user,
                    reS.getTimestamp("timeBorrow"),
                    reS.getTimestamp("timeReturn"),
                    reS.getDouble("latelyFee"),
                    reS.getDouble("costPerDayLate")
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
        Bill bill = (Bill) entity;

        String sql = "INSERT INTO bills (documentId, userId, timeBorrow, " +
                "timeReturn, latelyFee, costPerDayLate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, bill.getDocument().getDocumentId());
            prS.setString(2, bill.getUser().getUserId());
            prS.setTimestamp(3, bill.getTimeBorrow());
            prS.setTimestamp(4, bill.getTimeReturn());
            prS.setDouble(5, bill.getLatelyFee());
            prS.setDouble(6, bill.getCostPerDayLate());

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
        String sql = "SELECT * FROM bills WHERE billId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, Integer.parseInt(billId));
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
     * Find bill by user ID.
     *
     * @param userId user ID to find
     * @return bill
     */
    @Override
    public ObservableList<Bill> findByName(String userId) {
        ObservableList<Bill> bills = FXCollections.observableArrayList();
        String sql = "SELECT * FROM bills WHERE userId = ?";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, userId);
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                Bill bill = make(reS);
                bills.add(bill);
            }

            if (bills.isEmpty()) {
                System.out.println("No Bill found with userId: " + userId);
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
    public void update(Bill entity) {
        Bill bill = (Bill) entity;
        String sql = "UPDATE bills SET documentId = ?, userId = ?, timeBorrow = ?, timeReturn = ?, latelyFee = ?, costPerDayLate = ? WHERE billId = ?";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, bill.getDocument().getDocumentId());
            prS.setString(2, bill.getUser().getUserId());
            prS.setTimestamp(3, bill.getTimeBorrow());
            prS.setTimestamp(4, bill.getTimeReturn());
            prS.setDouble(5, bill.getLatelyFee());
            prS.setDouble(6, bill.getCostPerDayLate());
            prS.setInt(7, bill.getBillId());

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

package com.hunghq.librarymanagement.Connectivity;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection connection = MySQLConnection.getConnection();

        /*
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Kết nối đã được đóng.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        */


    }
}
package com.hunghq.librarymanagement.Connectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/lmsofficial";
    private static final String USER = "root";
    private static final String PASSWORD = "Trinh2003@@";

    @SuppressWarnings("CallToPrintStackTrace")
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            //System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            //System.out.println("Kết nối thất bại!");
            e.printStackTrace();
        }
        return connection;
    }
}

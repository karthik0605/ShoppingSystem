package com.shopmatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // Update with your MySQL credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/shopmatic?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";     // your DB username
    private static final String PASS = "password"; // your DB password

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        return connection;
    }
}

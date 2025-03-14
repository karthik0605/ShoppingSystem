package src.ui.queries;

import src.DatabaseManager;

import java.sql.*;

public class CustomerQueries {
    public static void insertCustomer(String name, String address) {
        String query = "INSERT INTO Customers(cname, address) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, address);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Customer added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

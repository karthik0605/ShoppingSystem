package src;

import java.sql.*;

public class CustomerQueries {
    public static void insertCustomer(String name, String address) {
        String query = "INSERT INTO Customers(cname, address) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);     // Set cname
            stmt.setString(2, address);  // Set address

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Customer added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getCustomers() {
        String query = "SELECT * FROM Customers";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("cID") +
                        ", Name: " + rs.getString("cname") +
                        ", Address: " + rs.getString("address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

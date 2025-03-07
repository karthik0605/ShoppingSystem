package src;

import java.sql.*;

public class CartedQueries {
    public static void insertCartedItem(int customerID, int itemID, int quantity) {
        String query = "INSERT INTO Carted(cID, iID, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerID);  // Set cID
            stmt.setInt(2, itemID);      // Set iID
            stmt.setInt(3, quantity);    // Set quantity

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Item added to cart successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getCartedItems() {
        String query = "SELECT * FROM Carted";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("Customer ID: " + rs.getInt("cID") +
                        ", Item ID: " + rs.getInt("iID") +
                        ", Quantity: " + rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


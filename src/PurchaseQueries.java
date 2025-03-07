package src;

import java.sql.*;

public class PurchaseQueries {
    public static void insertPurchase(int customerID) {
        String query = "INSERT INTO Purchases(cID) VALUES (?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerID);  // Set cID

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Purchase recorded successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getPurchases() {
        String query = "SELECT * FROM Purchases";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("Purchase ID: " + rs.getInt("pID") +
                        ", Customer ID: " + rs.getInt("cID") +
                        ", Date: " + rs.getTimestamp("date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


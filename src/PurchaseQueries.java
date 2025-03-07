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

    public static void makePayment(int customerID) {
        String query = "UPDATE Items i\n" +
                "JOIN Carted c ON c.iID = i.iID\n" +
                "SET i.stock = i.stock - c.quantity\n" +
                "WHERE c.cID = ?;";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerID);  // Set cID

            int rowsChanged = stmt.executeUpdate();
            if (rowsChanged > 0) {
                System.out.println("Purchase recorded successfully!");
                clearCart(customerID);
            }
            else {
                System.out.println("No items in cart");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearCart(int customerID) {
        String query = "DELETE FROM Carted WHERE cID = ?;";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerID);
            int deleteRows = stmt.executeUpdate();
            if (deleteRows > 0) {
                System.out.println(deleteRows + " item(s) removed from cart!");
            } else {
                System.out.println("Cart was already empty.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


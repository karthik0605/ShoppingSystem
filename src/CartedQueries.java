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

    public static void getCustomerCartPrice(int customerID) {
        String query = "SELECT SUM(i.price * c.quantity) AS total_price FROM Carted c JOIN Items i ON c.iID = i.iID WHERE c.cID = ?;";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // Use if instead of while (as there's only one result)
                    System.out.println("Price: " + rs.getDouble("total_price"));
                } else {
                    System.out.println("No items found in cart.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getCustomerCartItems(int customerID) {
        String query = "SELECT iname, price, quantity\n" +
                "FROM Carted c\n" +
                "JOIN Items i ON c.iID = i.iID\n" +
                "WHERE c.cID = ?;";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // Use if instead of while (as there's only one result)
                    System.out.println("Name: " + rs.getString("iname") +
                            ", Price: " + rs.getBigDecimal("price") +
                            ", Quantity: " + rs.getString("quantity"));
                } else {
                    System.out.println("No items found in cart.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


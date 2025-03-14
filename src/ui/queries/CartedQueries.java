package src.ui.queries;

import src.DatabaseManager;
import src.ui.Tuples.Item;
import src.ui.Tuples.CartedItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartedQueries {
    public static void insertCartedItem(int customerID, int itemID, int quantity) {
        String query = "INSERT INTO Carted (cID, iID, quantity)\n" +
                "VALUES (?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity);";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerID);
            stmt.setInt(2, itemID);
            stmt.setInt(3, quantity);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Item added to cart successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double getCustomerCartPrice(int customerID) {
        String query = "SELECT SUM(i.price * c.quantity) AS total_price FROM Carted c JOIN Items i ON c.iID = i.iID WHERE c.cID = ?;";
        double price = -1;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    price = rs.getDouble("total_price");
                } else {
                    System.out.println("No items found in cart.");
                    price = 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;
    }

    public static List<CartedItem> getCustomerCartItems(int customerID) {
        String query = "SELECT i.iname, i.price, c.quantity\n" +
                "FROM Carted c\n" +
                "JOIN Items i ON c.iID = i.iID\n" +
                "WHERE c.cID = ?;";
        List<CartedItem> cartedItemList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("i.iname");
                    double price = rs.getDouble("i.price");
                    int quantity = rs.getInt("c.quantity");
                    CartedItem cartedItem = new CartedItem(name, price, quantity);
                    cartedItemList.add(cartedItem);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartedItemList;
    }
}

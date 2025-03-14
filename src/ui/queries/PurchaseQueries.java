package src.ui.queries;

import src.DatabaseManager;

import java.sql.*;

import java.util.List;

import java.util.ArrayList;

import src.ui.Tuples.Purchase;

public class PurchaseQueries {
    public static void insertPurchase(int customerID, double price) {
        String query = "INSERT INTO Purchases(cID, price) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerID);  // Set cID
            stmt.setDouble(2, price);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Purchase recorded successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Purchase> getPurchases(int customerID) {
        String query = "SELECT * FROM Purchases WHERE cID = ?;";
        List<Purchase> purchaseList = new ArrayList<Purchase>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    int pID = rs.getInt("cID");
                    String date = rs.getString("date");
                    double price = rs.getDouble("price");
                    Purchase purchase = new Purchase(pID, date, price);
                    purchaseList.add(purchase);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchaseList;
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
                //clearCart(customerID);
            }
            else {
                System.out.println("No items in cart");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


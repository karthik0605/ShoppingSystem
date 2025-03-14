package src.ui.queries;

import javafx.collections.ObservableList;
import src.DatabaseManager;

import src.ui.Tuples.Item;

import java.util.ArrayList;

import java.util.List;

import java.sql.*;

public class ItemQueries {

    public static void insertItem(String name, double price, String description, String category, int stock) {
        String query = "INSERT INTO Items(iname, price, description, catname, stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);     // Set iname
            stmt.setDouble(2, price);    // Set price
            stmt.setString(3, description);  // Set description
            stmt.setString(4, category);  // Set catID
            stmt.setInt(5, stock);       // Set stock

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Item added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Item> getItems() {
        String query = "SELECT * FROM Items";
        List<Item> itemList = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int iID = rs.getInt("iID");
                String name = rs.getString("iname");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                String category = rs.getString("catname");
                int stock = rs.getInt("stock");
                Item item = new Item(iID, name, price, description, category, stock);
                itemList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    public static void getItemsByID(int iID) {
        String query = "SELECT *\n" +
                "FROM Items i\n" +
                "WHERE iID = ?;";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, iID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("iID") +
                            ", Name: " + rs.getString("iname") +
                            ", Price: " + rs.getBigDecimal("price") +
                            ", Description: " + rs.getString("description") +
                            ", Category: " + rs.getString("catname") +
                            ", Stock: " + rs.getInt("stock"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Item> getItemsByCategoryName(String catname) {
        String query = "SELECT * FROM Items WHERE catname = ?";
        List<Item> itemList = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, catname);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itemList.add(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    private static Item mapResultSetToItem(ResultSet rs) throws SQLException {
        return new Item(
                rs.getInt("iID"),
                rs.getString("iname"),
                rs.getDouble("price"),
                rs.getString("description"),
                rs.getString("catname"),
                rs.getInt("stock")
        );
    }
}

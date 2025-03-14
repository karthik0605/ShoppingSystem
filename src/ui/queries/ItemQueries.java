package src.ui.queries;

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

    public static Item getItemByID(int itemID) {
        String query = "SELECT *\n" +
                "FROM Items i\n" +
                "WHERE iID = ?;";

        Item item = null;  // Initialize item to null
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, itemID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {  // Only fetch one item, if it exists
                    int iID = rs.getInt("iID");
                    String name = rs.getString("iname");
                    double price = rs.getDouble("price");
                    String description = rs.getString("description");
                    String category = rs.getString("catname");
                    int stock = rs.getInt("stock");
                    item = new Item(iID, name, price, description, category, stock);  // Set the item object
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;  // Return the single item or null if not found
    }


    public static List<Item> getItemsByNameSubstring(String substring) {
        // Ensure the substring has the '%' for LIKE matching
        String formattedSubstring = "%" + substring + "%";  // Format the substring for LIKE query

        String query = "SELECT * FROM Items WHERE iname LIKE ?;";  // Use '?' placeholder

        List<Item> itemList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the formatted substring (with '%' symbols) in the query
            stmt.setString(1, formattedSubstring);  // This binds the parameter with the '%' added

            try (ResultSet rs = stmt.executeQuery()) {
                // Process the results
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
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemList;
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

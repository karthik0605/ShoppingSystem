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
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, description);
            stmt.setString(4, category);
            stmt.setInt(5, stock);

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

        Item item = null;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, itemID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int iID = rs.getInt("iID");
                    String name = rs.getString("iname");
                    double price = rs.getDouble("price");
                    String description = rs.getString("description");
                    String category = rs.getString("catname");
                    int stock = rs.getInt("stock");
                    item = new Item(iID, name, price, description, category, stock);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }


    public static List<Item> getItemsByNameSubstring(String substring) {
        // Ensure the substring has the '%' for LIKE matching
        String formattedSubstring = "%" + substring + "%";

        String query = "SELECT * FROM Items WHERE iname LIKE ?;";

        List<Item> itemList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, formattedSubstring);

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

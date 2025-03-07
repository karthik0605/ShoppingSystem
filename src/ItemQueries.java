package src;

import java.sql.*;

public class ItemQueries {
    public static void insertItem(String name, double price, String description, int categoryID, int stock) {
        String query = "INSERT INTO Items(iname, price, description, catID, stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);     // Set iname
            stmt.setDouble(2, price);    // Set price
            stmt.setString(3, description);  // Set description
            stmt.setInt(4, categoryID);  // Set catID
            stmt.setInt(5, stock);       // Set stock

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Item added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getItems() {
        String query = "SELECT * FROM Items";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("iID") +
                        ", Name: " + rs.getString("iname") +
                        ", Price: " + rs.getBigDecimal("price") +
                        ", Description: " + rs.getString("description") +
                        ", Category ID: " + rs.getInt("catID") +
                        ", Stock: " + rs.getInt("stock"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

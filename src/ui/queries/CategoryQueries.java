package src.ui.queries;

import src.DatabaseManager;

import java.sql.*;

public class CategoryQueries {
    public static void insertCategory(String name) {
        String query = "INSERT INTO Categories(catname) VALUES (?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Category added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getCategories() {
        String query = "SELECT * FROM Categories";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("catID") +
                        ", Name: " + rs.getString("catname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

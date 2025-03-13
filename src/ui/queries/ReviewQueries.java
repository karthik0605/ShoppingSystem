package src.ui.queries;

import src.DatabaseManager;

import java.sql.*;

public class ReviewQueries {
    public static void insertReview(int itemID, String review) {
        String query = "INSERT INTO Reviews(iid, review) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            stmt.setString(2, review);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Review added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getReviewsByItem(int itemID) {
        String query = "SELECT *\n" +
                "FROM Reviews i\n" +
                "WHERE iid = ?;";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, itemID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("iID") +
                            ", Review: " + rs.getString("review"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


package src.ui.queries;

import src.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<String> getReviewsByItem(int itemID) {
        String query = "SELECT *\n" +
                "FROM Reviews i\n" +
                "WHERE iid = ?;";
        List<String> reviews = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, itemID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String review = rs.getString("review");
                    reviews.add(review);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}


package src;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void createTables() {
        // Each CREATE TABLE statement includes IF NOT EXISTS for safety
        String createCustomers = 
            "CREATE TABLE IF NOT EXISTS Customers (" +
            " cID INT PRIMARY KEY AUTO_INCREMENT," +
            " cname VARCHAR(100) NOT NULL," +
            " address VARCHAR(100) NOT NULL" +
            ");";

        String createCategories = 
            "CREATE TABLE IF NOT EXISTS Categories (" +
            " catID INT PRIMARY KEY AUTO_INCREMENT," +
            " catname VARCHAR(100) UNIQUE NOT NULL" +
            ");";

        String createItems = 
            "CREATE TABLE IF NOT EXISTS Items (" +
            " iID INT PRIMARY KEY AUTO_INCREMENT," +
            " iname VARCHAR(100) NOT NULL," +
            " price DECIMAL(10, 2) NOT NULL," +
            " description VARCHAR(200) NOT NULL," +
            " catID INT NOT NULL," +
            " stock INT NOT NULL," +
            " FOREIGN KEY (catID) REFERENCES Categories(catID)" +
            ");";

        String createCarted = 
            "CREATE TABLE IF NOT EXISTS Carted (" +
            " cID INT NOT NULL," +
            " iID INT NOT NULL," +
            " quantity INTEGER NOT NULL DEFAULT 1," +
            " PRIMARY KEY (cID, iID)," +
            " FOREIGN KEY (cID) REFERENCES Customers(cID)," +
            " FOREIGN KEY (iID) REFERENCES Items(iID)" +
            ");";

        String createPurchases = 
            "CREATE TABLE IF NOT EXISTS Purchases (" +
            " pID INT PRIMARY KEY AUTO_INCREMENT," +
            " cID INT NOT NULL," +
            " date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            " FOREIGN KEY (cID) REFERENCES Customers(cID)" +
            ");";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createCustomers);
            stmt.executeUpdate(createCategories);
            stmt.executeUpdate(createItems);
            stmt.executeUpdate(createCarted);
            stmt.executeUpdate(createPurchases);

            System.out.println("All tables created (if they did not already exist).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

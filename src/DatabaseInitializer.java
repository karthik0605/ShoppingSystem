package src;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void createTables() {
        String createCustomers = 
            "CREATE TABLE Customers (\n" +
                    "\tcID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "    cname VARCHAR(100) NOT NULL,\n" +
                    "    address VARCHAR(100) NOT NULL\n" +
                    ");";

        String createItems = 
            "CREATE TABLE Items (\n" +
                    "\tiID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "    iname VARCHAR(100) NOT NULL,\n" +
                    "    price DECIMAL(10, 2) NOT NULL,\n" +
                    "    description VARCHAR(200) NOT NULL,\n" +
                    "    catname ENUM('Men', 'Women', 'House Appliance', 'Plants', 'Electronics'),\n" +
                    "    stock INT NOT NULL\n" +
                    ");";

        String createCarted = 
            "CREATE TABLE Carted (\n" +
                    "\tcID INT NOT NULL,\n" +
                    "    iID INT NOT NULL,\n" +
                    "    quantity INTEGER NOT NULL DEFAULT 1,\n" +
                    "    PRIMARY KEY (cID, iID),\n" +
                    "    FOREIGN KEY (cID) REFERENCES Customers(cID),\n" +
                    "    FOREIGN KEY (iID) REFERENCES Items(iID)\n" +
                    ");";

        String createPurchases = 
            "CREATE TABLE Purchases (\n" +
                    "\tpID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "\tcID INT NOT NULL,\n" +
                    "    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                    "    price double NOT NULL,\n" +
                    "    FOREIGN KEY (cID) REFERENCES Customers(cID)\n" +
                    ");";

        String createReviews =
                "CREATE TABLE Reviews (\n" +
                        "\trID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                        "\tiID INT NOT NULL,\n" +
                        "    review varchar(200) NOT NULL,\n" +
                        "    FOREIGN KEY (iID) REFERENCES Items(iID)\n" +
                        ");";

        String createTrigger =
                        "\n" +
                        "CREATE TRIGGER after_purchase_insert\n" +
                        "    AFTER INSERT ON Purchases\n" +
                        "    FOR EACH ROW\n" +
                        "BEGIN\n" +
                        "    DELETE FROM Carted WHERE cID = NEW.cID;\n" +
                        "    END    ;";
                ;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createCustomers);
            stmt.executeUpdate(createItems);
            stmt.executeUpdate(createCarted);
            stmt.executeUpdate(createPurchases);
            stmt.executeUpdate(createReviews);
            stmt.executeUpdate(createTrigger);


            System.out.println("All tables created (if they did not already exist).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

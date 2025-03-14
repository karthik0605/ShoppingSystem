package src;
import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://ambari-node5.csc.calpoly.edu/jdraj";
    private static final String USER = "jdraj";
    private static final String PASSWORD = "029788505";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

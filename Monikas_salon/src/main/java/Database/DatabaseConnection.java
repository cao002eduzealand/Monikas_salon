package Database;
import java.sql.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/Monika";
    private static final String USER = "root";
    private static final String PASSWORD = "root";


    // Method to get connection.
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
            e.printStackTrace();
            return null;
        }
    }
}


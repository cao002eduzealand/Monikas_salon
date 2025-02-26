package Database;
import java.sql.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/Monika";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

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
//public static Connection getConnection() {
//    try {
//        Class.forName("com.mysql.cj.jdbc.Driver"); // Sikrer, at JDBC-driveren loades
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    } catch (ClassNotFoundException e) {
//        System.out.println("JDBC Driver not found.");
//        e.printStackTrace();
//        return null;
//    } catch (SQLException e) {
//        System.out.println("Fejl i databaseforbindelsen. Tjek login-info og om serveren kører.");
//        e.printStackTrace();
//        return null;
//    }
//}

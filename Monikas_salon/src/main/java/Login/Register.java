package Login;

import Database.DatabaseConnection;
import javafx.scene.control.Alert;
import Objects.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Alert.Alerts;

public class Register {

    // Registers a new employee if the employee name does not already exist in the database
    public boolean registerEmployee(Employee employee) throws SQLException {

        String checksql = "SELECT * FROM Employees WHERE name =?";
        String insertsql = "INSERT INTO Employees (name, password) VALUES (?,?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checksql)) {

            // Check if the employee already exists in the database
            checkStatement.setString(1, employee.getName());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                Alerts.showAlert(Alert.AlertType.ERROR, "Error", "Employee already exists");
                return false;
            }

            // Insert the new employee into the database
            try (PreparedStatement insertStatement = connection.prepareStatement(insertsql)) {
                insertStatement.setString(1, employee.getName());
                insertStatement.setString(2, employee.getPassword());

                int rowsInserted = insertStatement.executeUpdate();

                // If at least one row was inserted, registration was successful
                if (rowsInserted > 0) {
                    return true;
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error during registration: " + e.getMessage());
        }

        return false;
    }
}


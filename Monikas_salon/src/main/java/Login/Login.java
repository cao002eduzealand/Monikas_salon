package Login;

import Database.DatabaseConnection;
import javafx.scene.control.Alert;
import Objects.Employee;
import Objects.SessionManager;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    public boolean logIn(TextField name, TextField password) throws SQLException {

        // SQL query to check if there's a matching employee with the given username and password
        String sql = "SELECT * FROM Employees WHERE name = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Get the entered username and password from the TextFields
            String name1 = name.getText();
            String password1 = password.getText();

            // Set the username and password in the prepared statement
            preparedStatement.setString(1, name1);
            preparedStatement.setString(2, password1);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If a matching employee is found in the database
                if (resultSet.next()) {
                    // Retrieve employee details from the database result
                    int employeeId = resultSet.getInt("id");
                    String employeeName = resultSet.getString("name");
                    String employeePassword = resultSet.getString("password");

                    // Create an Employee object for the logged-in user
                    Employee loggedInEmployee = new Employee(employeeId, employeeName, employeePassword);

                    // Set the logged-in employee in the session manager
                    SessionManager.setCurrentEmployee(loggedInEmployee);

                    // Return true, login was successful
                    return true;
                } else {
                    // If no matching employee was found, return false
                    return false;
                }
            } catch (SQLException e) {
                // Handle exception during result set processing
                e.printStackTrace();
                System.out.println("Error An error occurred while processing the login result.");
            }
        } catch (SQLException e) {
            // Handle database connection or query issues
            e.printStackTrace();
            System.out.println("Database Error There was an issue connecting to the database. Please try again later.");
        }

        // Return false if login failed
        return false;
    }
}

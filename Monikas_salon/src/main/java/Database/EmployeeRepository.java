package Database;

import Objects.Booking;
import Objects.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {

    // CRUD operations for managing employees.

    // Method to create a new employee record in the database
    public void createEmployee(Employee employee) {

        // SQL query to insert a new employee into the Employees table
        String sql = "INSERT INTO Employees (name, password) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the employee's name and password into the prepared statement
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getPassword());

            // Execute the update to insert the employee into the database
            preparedStatement.executeUpdate();

            System.out.println("Employee created successfully!");

        } catch (SQLException e) {
            // Error handling in case of SQL issues
            System.out.println("Error creating employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to retrieve all employees from the database
    public List<Employee> readEmployees() {

        List<Employee> employees = new ArrayList<>();

        // SQL query to select all employees from the Employees table
        String sql = "SELECT * FROM Employees";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Loop through the result set and create Employee objects
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"));

                // Add the employee to the list
                employees.add(employee);
            }

        } catch (SQLException e) {
            // Error handling if the query fails or the result set cannot be processed
            System.out.println("Error reading employees: " + e.getMessage());
            e.printStackTrace();
        }

        // Return the list of employees
        return employees;
    }

    // Method to delete an employee from the database by their ID
    public void deleteEmployee(int id) {
        String sql = "DELETE FROM Employees WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the ID of the employee to be deleted
            preparedStatement.setInt(1, id);

            // Execute the delete operation
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("No employee found with ID " + id + ". Deletion failed.");
            }

        } catch (SQLException e) {
            // Error handling in case the deletion fails
            System.out.println("Error deleting employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to update an existing employee's details in the database
    public void updateEmployee(Employee employee) {
        String sql = "UPDATE Employees SET name = ?, password = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the employee's name, password, and ID in the prepared statement
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getPassword());
            preparedStatement.setInt(3, employee.getId()); // Ensure the ID is set to update the correct employee

            // Execute the update query
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee updated successfully!");
            } else {
                System.out.println("Employee update failed. No rows affected.");
            }

        } catch (SQLException e) {
            // Error handling if the update fails
            System.out.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

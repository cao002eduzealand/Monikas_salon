package Objects;

import Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {
//Attributtes
    private int id;
    private String name;
    private String password;

    // Constructor to initialize an employee object
    public Employee(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    // Getter and setter methods for employee ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Getter and setter methods for employee name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter methods for employee password
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    // Retrieves an employee from the database using the given employee ID
    public static Employee getEmployeeById(int employee_id) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, employee_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password")
                );
            }
        }
        return null;  // Return null if employee is not found
    }

    // Returns a string representation of the employee object
    @Override
    public String toString() {
        return id + " " + name;
    }
}
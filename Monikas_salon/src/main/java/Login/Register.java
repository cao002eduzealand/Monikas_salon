package Login;

import Database.DatabaseConnection;
import javafx.scene.control.Alert;
import Objects.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register {

    public boolean registerEmployee (Employee employee) throws SQLException {

        String checksql = "SELECT * FROM Employees WHERE name =?";

        String insertsql = "INSERT INTO Employees (name, password) VALUES (?,?)";


        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement checkStatement = connection.prepareStatement(checksql)) {

            checkStatement.setString(1, employee.getName());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                showBadAlert("Error", "Employee already exists");
                return false;
            }

            try (PreparedStatement insertStatement = connection.prepareStatement(insertsql)) {
                insertStatement.setString(1, employee.getName());
                insertStatement.setString(2, employee.getPassword());
                insertStatement.executeUpdate();
                showGoodAlert("Success", "Employee registered successfully");
            }

        } catch (SQLException e) {
                e.printStackTrace();
        }

        return false;
    }

    private void showGoodAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showBadAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}




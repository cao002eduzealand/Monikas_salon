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

        String sql = "SELECT * FROM Employees WHERE name = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {


            String name1 = name.getText();
            String password1 = password.getText();


            preparedStatement.setString(1, name1);
            preparedStatement.setString(2, password1);


            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    showGoodAlert("Succes!", "Login Successful");


                    int Employee_id = resultSet.getInt("id");
                    String employeeName = resultSet.getString("name");
                    String employeePassword = resultSet.getString("password");

                    Employee loggedInEmployee = new Employee(Employee_id, employeeName, employeePassword);

                    SessionManager.setCurrentEmployee(loggedInEmployee);
                    return true;

                } else {
                   showBadAlert("Error", "Login Failed");
                   return false;
                }
            } catch (SQLException e){
                e.printStackTrace();

            }
        }
        return false;
    }
    private void showBadAlert(String title, String message){

        Alert alert = new  Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

    }
    private void showGoodAlert(String title, String message){

        Alert alert = new  Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

    }
}

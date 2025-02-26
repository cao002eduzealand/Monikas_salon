package Controllers;

import Login.Login;
import Login.Register;
import Objects.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }



    @FXML
    private Label Login;
    @FXML
    private Label Name;

    @FXML
    private Label Password;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField nameField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button exitButton;

    @FXML
    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
    }


    @FXML
    public void switchToMenu() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/monikas_salon/Menu.fxml"));
        Parent root = fxmlLoader.load();

        MenuController menuController = fxmlLoader.getController();
        menuController.setStage(stage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void login() {
        System.out.println("Login button clicked!"); // Debugging message

        Login login = new Login();

        if (nameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showAlert("Error", "Username and password cannot be empty!");
            System.out.println("Login failed: Empty fields!");
            return;
        }

        try {
            System.out.println("Attempting to log in..."); // Debugging message
            boolean isLogInSuccessful = login.logIn(nameField, passwordField);

            if (isLogInSuccessful) {
                System.out.println("Login successful! Switching scene...");

                nameField.clear();
                passwordField.clear();

                switchToMenu();

            } else {
                showAlert("Error", "Invalid credentials!");
                System.out.println("Login failed: Invalid credentials!");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    @FXML
    private void register() {
        Register register = new Register();
        if (nameField.getText().isEmpty() || passwordField.getText().isEmpty()){
            return;
        }
        Employee employee = new Employee(0, nameField.getText(), passwordField.getText());

        try {
            boolean isRegisterSuccesful = register.registerEmployee(employee);
            if (isRegisterSuccesful){

                nameField.clear();
                passwordField.clear();

            }
            else showAlert("Error", "bob");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
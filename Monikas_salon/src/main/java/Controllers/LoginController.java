package Controllers;

import Login.Login;
import Login.Register;
import Objects.Employee;
import Services.BookingService;
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
import Alert.Alerts;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    private Stage stage;

    // Setter for the stage (window) reference
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // FXML UI components
    @FXML
    private Label Login;
    @FXML
    private Label Name;
    @FXML
    private Button shutDownButton;
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

    // Initialize method to delete old bookings at app startup
    @FXML
    private void initialize() {
        BookingService bookingService = new BookingService();
        bookingService.deleteOldBookings(); // Removes old bookings to keep data up to date
    }

    // Switch to the main menu scene after a successful login
    @FXML
    public void switchToMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/monikas_salon/Menu.fxml"));
        Parent root = fxmlLoader.load(); // Load the Menu scene

        MenuController menuController = fxmlLoader.getController();
        menuController.setStage(stage); // Set the stage for the Menu

        Scene scene = new Scene(root); // Create new scene for the Menu
        stage.setScene(scene); // Set the scene
        stage.show(); // Show the Menu window
    }

    // Handle login action: validates input and calls the login service
    @FXML
    private void login() {
        System.out.println("Login button clicked!"); // Debugging message

        Login login = new Login();

        // Check if the username or password fields are empty
        if (nameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Alerts.showAlert(Alert.AlertType.ERROR, "Error", "Username and password cannot be empty!");
            System.out.println("Login failed: Empty fields!");
            return; // Stop further processing if fields are empty
        }

        try {
            System.out.println("Attempting to log in..."); // Debugging message
            boolean isLogInSuccessful = login.logIn(nameField, passwordField); // Try to log in with provided credentials

            if (isLogInSuccessful) {
                System.out.println("Login successful! Switching scene...");

                // Clear the input fields upon successful login
                nameField.clear();
                passwordField.clear();

                // Switch to the main menu after successful login
                switchToMenu();
            } else {
                Alerts.showAlert(Alert.AlertType.ERROR, "Error", "Invalid credentials!"); // Show error alert for invalid login
                passwordField.clear(); // Clear password field for security
                // System.out.println("Login failed: Invalid credentials!"); // Debugging message
            }

        } catch (SQLException e) {
            // Handle SQL-related exceptions
            e.printStackTrace();
            Alerts.showAlert(Alert.AlertType.ERROR, "Database Error", "There was an issue connecting to the database. Please try again later.");
            System.out.println("SQLException occurred: " + e.getMessage());
        } catch (IOException e) {
            // Handle file-related exceptions (for switching scenes)
            e.printStackTrace();
            Alerts.showAlert(Alert.AlertType.ERROR, "File Error", "There was an issue loading the next scene. Please try again later.");
            System.out.println("IOException occurred: " + e.getMessage());
        }
    }

    // Handle employee registration action: validates input and calls the register service
    @FXML
    private void register() {
        Register register = new Register();

        // Check if the username or password fields are empty
        if (nameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Alerts.showAlert(Alert.AlertType.ERROR, "Error", "Username and password cannot be empty!");
            return; // Stop further processing if fields are empty
        }

        Employee employee = new Employee(0, nameField.getText(), passwordField.getText());

        try {
            boolean isRegisterSuccessful = register.registerEmployee(employee); // Attempt to register the employee

            if (isRegisterSuccessful) {
                // Clear fields after successful registration
                nameField.clear();
                passwordField.clear();

                Alerts.showAlert(Alert.AlertType.INFORMATION, "Success", "Employee registered successfully!");
            } else {
                Alerts.showAlert(Alert.AlertType.ERROR, "Registration Error", "Employee registration failed! Please try again.");
            }

        } catch (SQLException e) {
            // Handle SQL-related exceptions during registration
            e.printStackTrace();
            Alerts.showAlert(Alert.AlertType.ERROR, "Database Error", "There was an issue registering the employee. Please try again later.");
            System.out.println("SQLException occurred during registration: " + e.getMessage());
        }
    }

    // Handle shutdown button action: closes the application
    @FXML
    private void shutDown() {
        System.exit(0); // Exits the application
    }
}

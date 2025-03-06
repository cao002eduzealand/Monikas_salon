package Controllers;

import Database.EmployeeRepository;
import Objects.Employee;
import Objects.SessionManager;
import Services.BookingService;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Alert.Alerts;
import java.io.IOException;

public class AdminEmployeeController {

    private Stage stage;
    private Scene previousScene;
    private MenuController menuController;
    private BookingService bookingService = new BookingService();

    // Set the stage for the controller (used for scene management)
    public void setStage(Stage stage) {
        this.stage = stage;
        if (stage == null) {
            System.err.println("Stage is null!"); // Debugging stage null check
        }
    }

    // Set the MenuController (to handle menu actions such as logout)
    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    // Set the previous scene to navigate back to
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    // FXML elements to interact with UI components
    @FXML
    private TableView<Employee> EmployeeTable;

    @FXML
    private TableColumn<Employee, Integer> EmployeeIdColumn;

    @FXML
    private TableColumn<Employee, String> EmployeeNameColumn;

    @FXML
    private TableColumn<Employee, String> EmployeePasswordColumn;

    @FXML
    private Button addEmployee;

    @FXML
    private Button deleteEmployee;

    @FXML
    private Button updateEmployee;

    @FXML
    private Button backButton;

    // ObservableList to hold the employees data
    private ObservableList<Employee> employees;

    // Initialize method to set up the table columns and data
    @FXML
    private void initialize() {
        // Set the property value factories to bind columns to the employee data
        EmployeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        EmployeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        EmployeePasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Refresh the table data when initializing
        refreshTable();

        // Load the employee data from the BookingService
        employees = FXCollections.observableArrayList(bookingService.getEmployees());
        EmployeeTable.setItems(employees); // Set the data in the table
    }

    // Method to refresh the table by fetching the latest employee data
    private void refreshTable() {
        employees = FXCollections.observableArrayList(bookingService.getEmployees());
        EmployeeTable.setItems(employees);
    }

    // Method to handle adding a new employee
    public void addEmployee() {
        openEmployeeForm(null);  // Open the form for a new employee (null means new)
    }

    // Method to handle deleting an employee
    public void deleteEmployee() {
        Employee selectedEmployee = EmployeeTable.getSelectionModel().getSelectedItem();

        // Check if an employee is selected
        if (selectedEmployee == null) {
            Alerts.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to delete.");
            return;
        }

        // Check if the selected employee is the currently logged-in user
        if (selectedEmployee.getId() == (SessionManager.getCurrentEmployeeId())) {

            // Confirm deletion if the user is deleting their own record
            if (Alerts.showConfirmation("Confirmation", "Are you sure you want to delete this employee? You will be taken to the Login menu")) {
                // Delete the employee and switch to the login screen
                bookingService.deleteEmployee(selectedEmployee.getId());
                if (menuController != null) {
                    try {
                        menuController.switchToLogin(); // Switch to login screen
                    } catch (IOException e) {
                        System.out.println("Error navigating to login: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                refreshTable(); // Refresh the table to reflect the changes
            }
        }
        // Remove the employee from the table and delete them from the database
        employees.remove(selectedEmployee);
        bookingService.deleteEmployee(selectedEmployee.getId());
    }

    // Method to handle updating an employee
    public void updateEmployee() {
        Employee selectedEmployee = EmployeeTable.getSelectionModel().getSelectedItem();

        // Check if an employee is selected
        if (selectedEmployee == null) {
            Alerts.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to update.");
            return;
        }

        // Open the employee form pre-filled with the selected employee data for editing
        openEmployeeForm(selectedEmployee);
    }

    // Method to handle going back to the previous scene
    @FXML
    private void goBack() {
        // Check if there is a previous scene and switch to it
        if (previousScene != null) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(previousScene);
        } else {
            System.out.println("No previous scene available.");
        }
    }

    // Helper method to open the employee form for adding or updating an employee
    private void openEmployeeForm(Employee employee) {
        try {
            // Load the FXML form for adding/updating an employee
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/monikas_salon/EmployeeForm.fxml"));
            Parent root = loader.load();

            // Get the controller of the EmployeeForm and set the employee data if updating
            EmployeeFormController controller = loader.getController();
            if (employee != null) {
                controller.setEmployee(employee);  // Pass the existing employee for editing
            }

            // Create and show a new stage for the form (new window)
            Stage stage = new Stage();
            stage.setTitle((employee == null) ? "Add Employee" : "Update Employee"); // Set the title based on the action
            stage.setScene(new Scene(root));
            stage.showAndWait();  // Wait for the window to close before refreshing

            // Refresh the employee table after the form is closed
            refreshTable();

        } catch (IOException e) {
            // Error handling if the form cannot be loaded or an I/O error occurs
            System.out.println("Error opening employee form: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

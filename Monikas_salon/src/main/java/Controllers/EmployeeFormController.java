package Controllers;

import Database.EmployeeRepository;
import Objects.Employee;
import Services.BookingService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Alert.Alerts;

public class EmployeeFormController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private final BookingService bookingService = new BookingService();
    private Employee employee;  // Stores the employee to update (if any)
    private boolean isUpdating = false;  // Tracks if updating or adding

    // Set the employee data for updating
    public void setEmployee(Employee employee) {
        this.employee = employee;
        if (employee != null) {
            isUpdating = true;
            nameField.setText(employee.getName());
            passwordField.setText(employee.getPassword());
        }
    }

    @FXML
    private void saveEmployee() {
        String name = nameField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || password.isEmpty()) {
            Alerts.showAlert(Alert.AlertType.WARNING, "Invalid Input", "Name and Password cannot be empty.");
            return;
        }

        if (isUpdating) {
            // Fix: Ensure we keep the ID when updating
            employee.setName(name);
            employee.setPassword(password);
            bookingService.updateEmployee(employee);
        } else {
            // Create new employee
            Employee newEmployee = new Employee(0, name, password);
            bookingService.createEmployee(newEmployee);
        }

        // Close the pop-up
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
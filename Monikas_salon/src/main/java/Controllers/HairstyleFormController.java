package Controllers;
import Alert.Alerts;

import Objects.Hairstyle;
import Services.BookingService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HairstyleFormController {

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Integer> durationComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;


    private Hairstyle hairstyle;
    private boolean isUpdating = false;

    BookingService bookingService = new BookingService();


    // Set the hairstyle data for updating
    public void setHairstyle(Hairstyle hairstyle) {
        this.hairstyle = hairstyle;
        if (hairstyle != null) {
            isUpdating = true;
            nameField.setText(hairstyle.getName());
            durationComboBox.setValue(hairstyle.getDuration());


        }
    }

    @FXML
    private void initialize() {
        // Populate duration combo box
        ObservableList<Integer> durationOptions = FXCollections.observableArrayList(15, 20, 30, 45, 60, 80, 90, 120);
        durationComboBox.setItems(durationOptions);
    }


        @FXML
        private void saveHairstyle() {
            String name = nameField.getText();
            Integer duration = durationComboBox.getValue();

            if (name.isEmpty() || duration == null) {
                Alerts.showAlert(Alert.AlertType.WARNING, "Invalid Input", "Name and Duration cannot be empty.");

                return;
            }

            if (isUpdating) {
                // Fix: Ensure we keep the ID when updating
                hairstyle.setName(name);
                hairstyle.setDuration(duration);
               bookingService.updateHairstyle(hairstyle);
                Alerts.showAlert(Alert.AlertType.CONFIRMATION, "Hairstyle Updated", "Hairstyle Updated successfully");

            } else {
                // Create new hairstyle
                Hairstyle newHairstyle = new Hairstyle(0, name, duration);
                bookingService.createHairstyle(newHairstyle);
                Alerts.showAlert(Alert.AlertType.INFORMATION, "Hairstyle Added", "Hairstyle added successfully!");

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

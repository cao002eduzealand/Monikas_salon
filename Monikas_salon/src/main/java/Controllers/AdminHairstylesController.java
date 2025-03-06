package Controllers;

import Objects.Hairstyle;
import Services.BookingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Alert.Alerts;

import java.io.IOException;

public class AdminHairstylesController {

    private Scene previousScene;
    private Stage stage;

    // Set the stage for the controller (used for scene management)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Set the previous scene to navigate back to
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    BookingService bookingService = new BookingService(); // Booking service to handle database operations

    // FXML elements for interacting with UI components
    @FXML
    private Button backButton;

    @FXML
    private TableView<Hairstyle> hairstyleTableView;

    @FXML
    private TableColumn<Hairstyle, Integer> hairstyleIDColumn;

    @FXML
    private TableColumn<Hairstyle, String> hairstyleNameColumn;

    @FXML
    private TableColumn<Hairstyle, Integer> hairstyleDurationColumn;

    private ObservableList<Hairstyle> hairstyles;

    @FXML
    private Button addHairstyleButton;

    @FXML
    private Button updateHairstyleButton;

    @FXML
    private Button deleteHairstyleButton;

    // Initialize method to set up the table columns and data
    @FXML
    private void initialize() {
        // Set the property value factories to bind columns to the hairstyle data
        hairstyleIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        hairstyleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        hairstyleDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        // Load the hairstyle data from the BookingService
        hairstyles = FXCollections.observableArrayList(bookingService.getHairstyles());
        hairstyleTableView.setItems(hairstyles); // Set the data in the table

        // Refresh the hairstyle list when initializing the view
        refreshHairstyles();
    }

    // Method to refresh the hairstyle table with the latest data
    private void refreshHairstyles() {
        hairstyles.setAll(bookingService.getHairstyles());
    }

    // Helper method to open the hairstyle form for adding or updating a hairstyle
    private void openHairstyleForm(Hairstyle hairstyle) {
        try {
            // Load the FXML form for adding/updating a hairstyle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/monikas_salon/HairstyleForm.fxml"));
            Parent root = loader.load();

            // Get the controller of the HairstyleForm and set the hairstyle data if updating
            HairstyleFormController controller = loader.getController();
            if (hairstyle != null) {
                controller.setHairstyle(hairstyle);  // Pass the existing hairstyle for editing
            }

            // Create and show a new stage for the form (new window)
            Stage stage = new Stage();
            stage.setTitle((hairstyle == null) ? "Add Hairstyle" : "Update Hairstyle"); // Set the title based on the action
            stage.setScene(new Scene(root));
            stage.showAndWait();  // Wait for the window to close before refreshing

            // Refresh the hairstyle table after the form is closed
            refreshHairstyles();

        } catch (IOException e) {
            // Error handling if the form cannot be loaded or an I/O error occurs
            System.out.println("Error opening hairstyle form: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to handle adding a new hairstyle
    @FXML
    private void addHairstyle() {
        openHairstyleForm(null);  // Open the form for a new hairstyle (null means new)
    }

    // Helper method to set the duration combo box options
    private ObservableList<Integer> setHairstyleDurationComboBox() {
        ObservableList<Integer> durations = FXCollections.observableArrayList();

        // Add durations in increments of 5 minutes from 5 to 120 minutes
        for (int i = 5; i <= 120; i += 5) {
            durations.add(i);
        }
        return durations;
    }

    // Method to handle updating an existing hairstyle
    @FXML
    public void updateHairstyle() {
        Hairstyle selectedHairstyle = hairstyleTableView.getSelectionModel().getSelectedItem();

        // Check if a hairstyle is selected
        if (selectedHairstyle == null) {
            Alerts.showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a hairstyle to update.");
            return;
        }

        // Open the form with the selected hairstyle data for updating
        openHairstyleForm(selectedHairstyle);
    }

    // Method to handle deleting a hairstyle
    @FXML
    public void deleteHairstyle() {
        Hairstyle selectedHairstyle = hairstyleTableView.getSelectionModel().getSelectedItem();

        // Check if a hairstyle is selected
        if (selectedHairstyle == null) {
            return; // No hairstyle selected, exit method
        }

        // Confirm deletion with the user
        if (Alerts.showConfirmation("Confirmation", "Are you sure you want to delete this hairstyle?")) {
            // Delete the hairstyle from the database and remove from the table
            bookingService.deleteHairstyle(selectedHairstyle.getId());
            hairstyles.remove(selectedHairstyle);
        }
    }

    // Method to handle the cancel (back) action
    @FXML
    private void cancel() {
        // Check if there is a previous scene and switch to it
        if (previousScene != null) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(previousScene);
        } else {
            System.out.println("Error: No previous scene available.");
        }
    }
}

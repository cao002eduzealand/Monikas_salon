package Controllers;

import Database.BookingRepository;
import Database.CreateFinishedBookingRepository;
import Database.HairstyleRepository;
import Objects.Booking;
import Objects.Employee;
import Objects.Hairstyle;
import Objects.SessionManager;
import Services.BookingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import Alert.Alerts;

public class CreateBookingController {

    private AdminBookingController adminBookingController;
    private Scene previousScene;
    private Stage stage;

    // Setters for stage and previous scene
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAdminBookingController(AdminBookingController adminBookingController) {
        this.adminBookingController = adminBookingController;
    }

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    private final BookingService bookingService = new BookingService(); // Booking service to manage bookings

    // FXML elements
    @FXML
    private Label createBookingLabel;

    @FXML
    private Button createBookingButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField customerNameField;

    @FXML
    private ComboBox<Hairstyle> hairStyleComboBox;

    @FXML
    private ComboBox<String> startTimeCombobox;

    @FXML
    private ComboBox<String> dateComboBox; // ComboBox for date selection

    @FXML
    private TextField endTimeField;

    private ObservableList<Hairstyle> hairstyles;
    private ObservableList<String> availableDates;

    // Method to initialize the controller and set up the ComboBoxes
    @FXML
    private void initialize() {
        // Generate and load future available dates for the next 30 days
        availableDates = generateFutureDates();
        dateComboBox.setItems(availableDates);
        dateComboBox.getSelectionModel().selectFirst(); // Default to the first available date

        // Load hairstyles from the service/database
        hairstyles = FXCollections.observableArrayList(bookingService.getHairstyles());
        hairStyleComboBox.setItems(hairstyles);

        // Update available time slots based on selected date
        updateAvailableTimes();

        // Listeners to dynamically update available times and end time when needed
        dateComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateAvailableTimes());
        hairStyleComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateEndTime());
        startTimeCombobox.valueProperty().addListener((observable, oldValue, newValue) -> updateEndTime());
    }

    // Generate the next 30 days of available dates for the booking
    private ObservableList<String> generateFutureDates() {
        ObservableList<String> dates = FXCollections.observableArrayList();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Generate the next 30 days
        for (int i = 0; i < 30; i++) {
            dates.add(today.plusDays(i).format(formatter));
        }

        return dates;
    }

    // Update available time slots based on selected date and employee's schedule
    private void updateAvailableTimes() {
        ObservableList<String> availableTimes = FXCollections.observableArrayList();
        LocalTime now = LocalTime.now();
        LocalTime startTime = LocalTime.of(8, 0); // Earliest available time slot
        LocalTime endTime = LocalTime.of(16, 0);  // Latest available time slot

        LocalDate selectedDate = LocalDate.parse(dateComboBox.getValue()); // Get the selected date
        Employee selectedEmployee = SessionManager.getCurrentEmployee(); // Get the currently logged-in employee

        if (selectedEmployee == null) {
            Alerts.showAlert(Alert.AlertType.ERROR, "Error", "No employee selected.");
            return;
        }

        // Fetch booked time slots for the selected employee on the selected date
        List<Timestamp[]> bookedSlots = bookingService.getBookedTimeSlots(selectedEmployee.getId(), selectedDate);

        // If today, adjust start time to the nearest available slot after the current time
        if (selectedDate.isEqual(LocalDate.now())) {
            int currentMinute = now.getMinute();
            int minutesToAdd = (20 - (currentMinute % 20)) % 20; // Find the next available 20-minute slot
            startTime = now.plusMinutes(minutesToAdd).withSecond(0).withNano(0);
        }

        // Loop through the available times and check if each slot is booked
        while (startTime.isBefore(endTime)) {
            boolean isAvailable = true;

            for (Timestamp[] slot : bookedSlots) {
                LocalTime bookedStart = slot[0].toLocalDateTime().toLocalTime();
                LocalTime bookedEnd = slot[1].toLocalDateTime().toLocalTime();

                if (!startTime.isBefore(bookedStart) && startTime.isBefore(bookedEnd)) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                availableTimes.add(startTime.toString());
            }

            startTime = startTime.plusMinutes(10); // Move to the next 10-minute slot
        }

        startTimeCombobox.setItems(availableTimes);
        if (!availableTimes.isEmpty()) {
            startTimeCombobox.getSelectionModel().selectFirst(); // Default to the first available time
        } else {
            Alerts.showAlert(Alert.AlertType.INFORMATION, "No Available Times", "No available time slots for this employee on the selected date.");
        }
    }

    // Update the end time based on selected start time and hairstyle duration
    private void updateEndTime() {
        String selectedStartTime = startTimeCombobox.getValue();
        Hairstyle selectedHairstyle = hairStyleComboBox.getValue();

        if (selectedStartTime != null && selectedHairstyle != null) {
            LocalTime startTime = LocalTime.parse(selectedStartTime);
            int hairstyleDuration = selectedHairstyle.getDuration();

            LocalTime endTime = startTime.plusMinutes(hairstyleDuration);
            endTimeField.setText(endTime.toString());
        }
    }

    // Method to handle booking creation
    @FXML
    private void createBooking() {
        String customerName = customerNameField.getText();
        Hairstyle selectedHairstyle = hairStyleComboBox.getValue();
        String selectedStartTime = startTimeCombobox.getValue();
        String selectedDateStr = dateComboBox.getValue(); // Get selected date

        // Check if all required fields are filled
        if (customerName.isEmpty() || selectedHairstyle == null || selectedStartTime == null || selectedDateStr == null) {
            Alerts.showAlert(Alert.AlertType.INFORMATION, "Error", "Please fill in all fields before creating a booking!");
            return;
        }

        // Convert selected date and time to Timestamp
        LocalDate selectedDate = LocalDate.parse(selectedDateStr);
        LocalTime startTime = LocalTime.parse(selectedStartTime);
        LocalDateTime bookingDateTime = LocalDateTime.of(selectedDate, startTime);
        Timestamp startTimestamp = Timestamp.valueOf(bookingDateTime);

        // Calculate the end time based on hairstyle duration
        LocalDateTime endDateTime = bookingDateTime.plusMinutes(selectedHairstyle.getDuration());
        Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

        Employee employee = SessionManager.getCurrentEmployee();

        // Check if the employee is available during the selected time
        if (!bookingService.isEmployeeAvailable(employee.getId(), startTimestamp, endTimestamp)) {
            Alerts.showAlert(Alert.AlertType.INFORMATION, "Error", "This employee is already booked at this time. Please select a different time.");
            return;
        }

        // Create a new booking object and save it to the database
        Booking booking = new Booking(0, customerName, employee, selectedHairstyle, startTimestamp, endTimestamp);
        bookingService.createBooking(booking);
        Alerts.showAlert(Alert.AlertType.CONFIRMATION, "Success", "Booking created successfully!");

        // Refresh the bookings list in the admin view
        adminBookingController.refreshBookings();

        // Return to the previous scene
        if (previousScene != null) {
            Stage stage = (Stage) createBookingButton.getScene().getWindow();
            stage.setScene(previousScene);
        } else {
            System.out.println("Error: No previous scene stored!");
        }
    }

    // Method to handle cancel action and return to the previous scene
    @FXML
    public void cancel() {
        if (previousScene != null) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(previousScene);
        } else {
            System.out.println("Error: No previous scene available.");
        }
    }
}

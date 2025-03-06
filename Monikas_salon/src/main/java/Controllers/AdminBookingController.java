package Controllers;
import Database.BookingRepository;
import Database.CreateCancelledBookingRepository;
import Database.CreateFinishedBookingRepository;
import Objects.Booking;
import Objects.Employee;
import Objects.Hairstyle;
import Services.BookingService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Timestamp;
import Alert.Alerts;


public class AdminBookingController {


    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

   private final BookingService bookingService = new BookingService();



    @FXML
    private Button backButton;

    @FXML
    private Button switchToCreateBookingButton;

    @FXML
    private TableView<Booking> bookingTable;

    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;

    @FXML
    private TableColumn<Booking, String> customerNameColumn;

    @FXML
    private TableColumn<Booking, String> employeeNameColumn;

    @FXML
    private TableColumn<Booking, String> hairstyleColumn;

    @FXML
    private TableColumn<Booking, Timestamp> startTimeColumn;

    @FXML
    private TableColumn<Booking, Timestamp> endTimeColumn;

    private ObservableList<Booking> bookings;

    @FXML
    private Button cancelBookingButton;

    @FXML
    private Button createdBookingsButton;

    @FXML
    private Button finishedBookingsButton;

    @FXML
    private Button cancelledBookingsButton;

    // Initializes the controller and sets up the table data
    @FXML
    private void initialize() {

      bookingService.movePastBookingsToFinished();


        // Set up columns in the table view
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customer_name"));

        employeeNameColumn.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue().getEmployee();
            return new SimpleStringProperty((employee != null) ? employee.getName() : "Unknown");
        });

        hairstyleColumn.setCellValueFactory(cellData -> {
            Hairstyle hairstyle = cellData.getValue().getHairstyle();
            return new SimpleStringProperty((hairstyle != null) ? hairstyle.getName() : "Unknown");
        });

        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        bookings = FXCollections.observableArrayList(bookingService.getCreatedBookings());
        bookingTable.setItems(bookings);

        refreshBookings();
    }

    // Displays only created bookings
    @FXML
    private void showCreatedBookings() {
        bookings.setAll(bookingService.getCreatedBookings());
        bookingTable.setItems(bookings);

        cancelBookingButton.setDisable(false);
    }

    // Displays only finished bookings
    @FXML
    private void showFinishedBookings() {
        bookings.setAll(bookingService.getFinishedBookings());
        bookingTable.setItems(bookings);

        cancelBookingButton.setDisable(true);
    }

    // Displays only cancelled bookings
    @FXML
    private void showCancelledBookings() {
        bookings.setAll(bookingService.getCancelledBookings());
        bookingTable.setItems(bookings);

        cancelBookingButton.setDisable(true);
    }

    // Refreshes the bookings table
    public void refreshBookings(){
        bookings.setAll(bookingService.getCreatedBookings());
    }

    // Cancels the selected booking
    @FXML
    private void cancelBooking() {
        Booking selectedBooking = bookingTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            Alerts.showAlert(Alert.AlertType.ERROR, "No booking selected", "Please select a booking first");
            return;
        }

            if (Alerts.showConfirmation("Confirmation", "Are you sure you want to cancel booking?")) {

                // Flyt booking til cancelled bookings
                bookingService.cancelBooking(selectedBooking);

                // Fjern booking fra UI-listen
                bookings.remove(selectedBooking);

                // Opdater databasen ved at fjerne den fra aktive bookinger
                bookingService.deleteBookingFromBookings(selectedBooking.getId());
            }

    }

    // Switches to the main menu
    @FXML
    private void switchToMenu() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/monikas_salon/Menu.fxml"));
            Parent root = fxmlLoader.load();

            MenuController menuController = fxmlLoader.getController();
            menuController.setStage(stage);  // Set the stage in MenuController

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load menu: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Switches to the create booking screen
    @FXML
    public void switchToCreateBooking() {
        try{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/monikas_salon/CreateBooking.fxml"));
        Parent root = fxmlLoader.load();

        CreateBookingController createBookingController = fxmlLoader.getController();
        createBookingController.setStage(stage);
        createBookingController.setPreviousScene(bookingTable.getScene()); // Store previous scene
        createBookingController.setAdminBookingController(this); // Pass reference

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
            System.err.println("Failed to load create booking: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

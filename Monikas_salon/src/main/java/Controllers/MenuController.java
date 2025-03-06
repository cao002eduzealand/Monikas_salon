package Controllers;

import Objects.Employee;
import Objects.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;

public class MenuController {

    private Scene previousScene;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Region spacer;

    @FXML
    private Button hejButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button goToAdminHairstyleButton;

    @FXML
    private Button switchToAdminEmployeeButton;

    @FXML
    private Label showLoggedInEmployee;

    @FXML
    private Label seeAllBookings;

    // Initializes the menu, sets layout properties, and displays logged-in employee information
    @FXML
    public void initialize() {
        HBox.setHgrow(spacer, Priority.ALWAYS);
        showLoggedInEmployee.setText("Velkommen, " + SessionManager.getCurrentEmployee().getName() + "!");
    }

    // Switches the view to the booking management screen
    @FXML
    public void switchToBooking() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/monikas_salon/AdminBooking.fxml"));
        Parent root = fxmlLoader.load();

        AdminBookingController adminBookingController = fxmlLoader.getController();
        adminBookingController.setStage(stage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Logs out the current user and switches to the login screen
    @FXML
    public void switchToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/monikas_salon/Login.fxml"));
        Parent root = fxmlLoader.load();

        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(stage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Switches to the employee management screen
    @FXML
    public void switchToAdminEmployee() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/monikas_salon/AdminEmployee.fxml"));
        Parent root = fxmlLoader.load();

        AdminEmployeeController adminEmployeeController = fxmlLoader.getController();
        adminEmployeeController.setStage(stage);
        adminEmployeeController.setPreviousScene(stage.getScene());
        adminEmployeeController.setMenuController(this);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Switches to the hairstyle management screen
    @FXML
    public void switchToAdminHairstyle() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/monikas_salon/AdminHairstyle.fxml"));
        Parent root = fxmlLoader.load();

        AdminHairstylesController adminHairstylesController = fxmlLoader.getController();
        adminHairstylesController.setStage(stage);
        adminHairstylesController.setPreviousScene(stage.getScene());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

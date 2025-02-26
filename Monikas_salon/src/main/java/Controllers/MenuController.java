package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    private Button switchToCreateBookingButton;




    @FXML
    public void switchToCreateBooking() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/monikas_salon/CreateBooking.fxml"));
        Parent root = fxmlLoader.load();

        CreateBookingController createBookingController = fxmlLoader.getController();
        createBookingController.setStage(stage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

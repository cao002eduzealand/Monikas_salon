package Controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import javafx.scene.control.Label;


public class CreateBookingController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Label hejTekst;
}

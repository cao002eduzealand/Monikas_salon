package org.example.monikas_salon;

import Controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginApplication extends Application {

    // Starts the JavaFX application by loading the login screen
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("Login.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

        // Retrieve the login controller and set the stage
        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(stage);

        // Set window title and display the login screen
        stage.setTitle("Monika's Salon");
        stage.setScene(scene);
        stage.show();
    }

    // Main method to launch the JavaFX application
    public static void main(String[] args) {
        launch();
    }
}
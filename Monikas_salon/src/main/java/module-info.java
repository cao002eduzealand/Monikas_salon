module org.example.monikas_salon {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;

    opens org.example.monikas_salon to javafx.fxml;
    exports org.example.monikas_salon;
    exports Objects;
    opens Objects to javafx.fxml;
    exports Controllers;
    opens Controllers to javafx.fxml;
}
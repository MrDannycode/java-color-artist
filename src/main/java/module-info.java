module com.example.colorartist {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.colorartist to javafx.fxml;
    exports com.example.colorartist;
}
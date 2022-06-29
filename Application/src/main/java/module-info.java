module com.example.application {
    requires javafx.controls;
    requires javafx.fxml;


    opens application.view to javafx.fxml;
    exports application.view;
}
module com.example.discordapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.discordapp.View to javafx.fxml;
    exports com.discordapp.View;
}
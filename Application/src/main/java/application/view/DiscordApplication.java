package application.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DiscordApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DiscordApplication.class.getResource("signup-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 700);
        stage.setTitle("Discord");
        stage.getIcons().add(new Image("file:assets/discord_icon.png"));
        stage.setScene(scene);
        stage.show();
    }


    public static void loadNewScene(FXMLLoader loader, Stage stage) {
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
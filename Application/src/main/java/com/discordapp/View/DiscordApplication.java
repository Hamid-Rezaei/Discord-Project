package com.discordapp.View;
import com.discordapp.Controller.AppController;
import com.discordapp.Model.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

/**
 * The type Discord application.
 */
public class DiscordApplication extends Application {

    /**
     * The constant appController.
     */
    public static AppController appController;
    /**
     * The constant user.
     */
    public static User user;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DiscordApplication.class.getResource("animation-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Discord");
        stage.getIcons().add(new Image("file:/assets/discord_icon.png"));
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Load new scene.
     *
     * @param loader the loader
     * @param stage  the stage
     */
    public static void loadNewScene(FXMLLoader loader, Stage stage) {
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        Platform.setImplicitExit(true);
        super.stop();
        Platform.exit();
        System.exit(0);
    }

}
package com.discordapp.View;
import com.discordapp.Controller.AppController;
import com.discordapp.Model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class DiscordApplication extends Application {

    //public static User user;
    //public static AppController appController;

    public InApplicationViewController inApplicationViewController;

    public static AppController appController;
    public static User user;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DiscordApplication.class.getResource("signup-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Discord");
        stage.getIcons().add(new Image("file:assets/discord_icon.png"));
        stage.setScene(scene);
        stage.show();
        // loadAllController();
    }

    public void loadAllController(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
            Parent root = loader.load();
            inApplicationViewController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
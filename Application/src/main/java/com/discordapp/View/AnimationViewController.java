package com.discordapp.View;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * The type Animation view controller.
 */
public class AnimationViewController implements Initializable {

    @FXML
    private ImageView gif;

    @FXML
    private AnchorPane stackPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image load = new Image(new File("assets/gif.gif").toURI().toString());
        gif.setImage(load);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(6000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("signup-view.fxml"));
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setTitle("Discord");
                                stage.getIcons().add(new Image("file:assets/discord_icon.png"));
                                stage.setScene(scene);
                                stage.initStyle(StageStyle.DECORATED);
                                stage.show();
                                stackPane.getScene().getWindow().hide();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

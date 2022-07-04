package com.discordapp.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StatusViewController {

    public static Color color = Color.GREEN;

    @FXML
    void disturb(MouseEvent event) {
        color = Color.RED;
        closePopupStage(event);
    }

    @FXML
    void idle(MouseEvent event) {
        color = Color.YELLOW;
        closePopupStage(event);
    }

    @FXML
    void invisible(MouseEvent event) {
        color = Color.GRAY;
        closePopupStage(event);
    }

    @FXML
    void online(MouseEvent event) {
        color = Color.GREEN;
        closePopupStage(event);
    }

    @FXML
    public void closePopupStage(MouseEvent event) {
/*        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
            Parent root = loader.load();
            InApplicationViewController inApplicationViewController = loader.getController();
            inApplicationViewController.setStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}

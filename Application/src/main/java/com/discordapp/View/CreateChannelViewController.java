package com.discordapp.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.Stage;

public class CreateChannelViewController {

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getScene().getRoot();
        root.setEffect(null);
        stage.close();
    }

    @FXML
    void createChannel(ActionEvent event) {

        cancel(event);
    }

    @FXML
    void getChannelName(ActionEvent event) {

        cancel(event);
    }

}

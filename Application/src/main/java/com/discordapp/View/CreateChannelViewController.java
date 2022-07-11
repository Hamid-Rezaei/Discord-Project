package com.discordapp.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateChannelViewController {

    @FXML
    private TextField channelNameTF;

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getOwner().getScene().getRoot();
        root.setEffect(null);
        stage.close();
    }

    @FXML
    void createChannel(ActionEvent event) {
        
        cancel(event);
    }


}

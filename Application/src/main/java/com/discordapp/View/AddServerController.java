package com.discordapp.View;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddServerController {

    @FXML
    private TextField serverNameTF;
    private String serverName;

    public void initialize(String serverName){
        this.serverName = serverName;
    }


    @FXML
    private void closeAddServerPop(ActionEvent event) {
        closePopupStage(event);
    }

    @FXML
    public void createServer(ActionEvent event) {
        serverName = serverNameTF.getText();
        closePopupStage(event);
    }


    private void closePopupStage(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

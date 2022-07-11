package com.discordapp.View;


import com.discordapp.Model.TextChannel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.channels.Channel;

public class ServerSettingViewController {
    private ListView<TextChannel> listTxtChannel;

    @FXML
    public void initialize(ListView<TextChannel> listTxtChannel){
        this.listTxtChannel = listTxtChannel;
    }

    @FXML
    void changeName(ActionEvent event) {

    }

    @FXML
    void createChannel(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        Stage popupStage = new Stage(StageStyle.UTILITY);
        popupStage.initOwner(stage.getOwner());
        popupStage.initModality(Modality.APPLICATION_MODAL);
       // popupStage.setY(event.getScreenY() - 200);
       // popupStage.setX(event.getScreenX() - 200);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("create-channel-view.fxml"));
        Parent rot = loader.load();
        CreateChannelViewController cChc = loader.getController();
        cChc.initialize(listTxtChannel);
        popupStage.setScene(new Scene(rot, Color.TRANSPARENT));
        popupStage.show();
    }

    @FXML
    void deleteServer(ActionEvent event) {

    }

    @FXML
    void invitePeople(ActionEvent event) {

    }

}

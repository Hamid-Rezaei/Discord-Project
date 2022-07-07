package com.discordapp.View;

import com.discordapp.Model.Guild;
import com.discordapp.Model.GuildUser;
import com.discordapp.Model.Role;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddServerController {

    @FXML
    private TextField serverNameTF;
/*    private String serverName;

    public void initialize(String serverName) {
        this.serverName = serverName;
    }*/


    @FXML
    private void closeAddServerPop(ActionEvent event) {
        closePopupStage(event);
    }

    @FXML
    public void createServer(ActionEvent event) {
        String serverName = serverNameTF.getText();
        GuildUser owner = new GuildUser(DiscordApplication.user, new Role("owner"));
        Guild guild = new Guild(serverName, owner);
        DiscordApplication.appController.addServer(guild);
        closePopupStage(event);
    }


    private void closePopupStage(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
/*        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();*/
    }
}

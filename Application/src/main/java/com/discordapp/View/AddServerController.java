package com.discordapp.View;

import com.discordapp.Model.Guild;
import com.discordapp.Model.GuildUser;
import com.discordapp.Model.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddServerController {
    @FXML
    private TextField serverNameTF;

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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
        DiscordApplication.loadNewScene(loader, (Stage) stage.getOwner());
    }

}

package com.discordapp.View;

import com.discordapp.Model.Guild;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DeleteChannelViewController {

    @FXML
    private TextField channelNameTF;

    /**
     * Cancel.
     *
     * @param event the event
     */
    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getOwner().getScene().getRoot();
        root.setEffect(null);
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-guild-view.fxml"));
        DiscordApplication.loadNewScene(loader, (Stage) stage.getOwner());
    }

    /**
     * Delete Channel.
     *
     * @param event the event
     */
    @FXML
    void deleteChannel(ActionEvent event) {
        Guild currGuild = InGuildViewController.currGuild;
        String respond = DiscordApplication.appController.removeTextChannel(currGuild, channelNameTF.getText());
        System.out.println(respond);
        InGuildViewController.currGuild = DiscordApplication.appController.getGuild(currGuild.getOwnerName(), currGuild.getName());
        cancel(event);

    }

}

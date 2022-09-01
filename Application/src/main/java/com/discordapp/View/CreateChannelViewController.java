package com.discordapp.View;

import com.discordapp.Model.Guild;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The type Create channel view controller.
 */
public class CreateChannelViewController {

    @FXML
    private TextField channelNameTF;

    /**
     * Cancel.
     *
     * @param event the event
     */
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getOwner().getScene().getRoot();
        root.setEffect(null);
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-guild-view.fxml"));
        DiscordApplication.loadNewScene(loader, (Stage) stage.getOwner());
    }

    /**
     * Create channel
     *
     * @param event the event
     */
    @FXML
    private void createChannel(ActionEvent event) {
        Guild currGuild = InGuildViewController.currGuild;
        String respone = DiscordApplication.appController.addNewTextChannel(currGuild, channelNameTF.getText());
        System.out.println(respone);
        InGuildViewController.currGuild = DiscordApplication.appController.getGuild(currGuild.getOwnerName(), currGuild.getName());
        cancel(event);
    }

}

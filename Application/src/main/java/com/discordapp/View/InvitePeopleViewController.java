package com.discordapp.View;

import com.discordapp.Model.Guild;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The type Invite people view controller.
 */
public class InvitePeopleViewController {

    @FXML
    private Label GuildNameLb;

    @FXML
    private TextField friendNameTB;

    /**
     * Initialize.
     */
    @FXML
    public void initialize(){
        GuildNameLb.setText(InGuildViewController.currGuild.getName());
    }

    /**
     * Cancel.
     *
     * @param event the event
     */
    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getOwner().getScene().getRoot();
        root.setEffect(null);
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-guild-view.fxml"));
        DiscordApplication.loadNewScene(loader, (Stage) stage.getOwner());
    }

    /**
     * Invite Friend.
     *
     * @param event the event
     */
    @FXML
    private void invite(MouseEvent event) {
        Guild currGuild = InGuildViewController.currGuild;
        String respond = DiscordApplication.appController.addMemberToServer(friendNameTB.getText(), currGuild);
        System.out.println(respond);
        InGuildViewController.currGuild = DiscordApplication.appController.getGuild(currGuild.getOwnerName(), currGuild.getName());
        cancel(event);
    }

}

package com.discordapp.View;

import com.discordapp.Model.Guild;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DeleteServerViewController {

    @FXML
    private Label GuildNameLb;
    @FXML
    private Label error;

    @FXML
    public void initialize() {
        GuildNameLb.setText(InGuildViewController.currGuild.getName());
    }

    @FXML
    void cancel(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getOwner().getScene().getRoot();
        root.setEffect(null);
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
        DiscordApplication.loadNewScene(loader, (Stage) stage.getOwner());
    }

    @FXML
    void deleteServer(MouseEvent event) {
        Guild currGuild = InGuildViewController.currGuild;
        if (DiscordApplication.user.getUsername().equals(currGuild.getOwnerName())) {
            String response = DiscordApplication.appController.deleteGuild(currGuild, currGuild.getOwnerName());
            System.out.println(response);
            cancel(event);
        } else {
            error.setText("Only the owner can delete server.");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = stage.getOwner().getScene().getRoot();
            root.setEffect(null);
        }
    }

}

package com.discordapp.View;

import com.discordapp.Model.Guild;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The type Delete server view controller.
 */
public class DeleteServerViewController {

    @FXML
    private Label GuildNameLb;
    @FXML
    private Label error;

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        GuildNameLb.setText(InGuildViewController.currGuild.getName());
    }

    /**
     * Cancel.
     *
     * @param event the event
     */
    @FXML
    private void cancel(MouseEvent event) {
        closeStage(event, "in-guild-view.fxml");
    }

    /**
     * Delete Server.
     *
     * @param event the event
     */
    @FXML
    private void deleteServer(MouseEvent event) {
        Guild currGuild = InGuildViewController.currGuild;
        if (DiscordApplication.user.getUsername().equals(currGuild.getOwnerName())) {
            String response = DiscordApplication.appController.deleteGuild(currGuild, currGuild.getOwnerName());
            System.out.println(response);
            closeStage(event, "in-application-view.fxml");
        } else {
            error.setText("Only the owner can delete server.");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = stage.getOwner().getScene().getRoot();
            root.setEffect(null);
        }
    }

    /**
     * Close stage.
     */
    private void closeStage(MouseEvent event, String fxml) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getOwner().getScene().getRoot();
        root.setEffect(null);
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        DiscordApplication.loadNewScene(loader, (Stage) stage.getOwner());
    }
}

package com.discordapp.View;

import com.discordapp.Model.Guild;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The type Change server name view controller.
 */
public class ChangeServerNameViewController {

    @FXML
    private TextField nameTF;

    /**
     * Cancel.
     *
     * @param event the event
     */
    @FXML
    void cancel(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getOwner().getScene().getRoot();
        root.setEffect(null);
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-guild-view.fxml"));
        DiscordApplication.loadNewScene(loader, (Stage) stage.getOwner());
    }

    /**
     * Change name.
     *
     * @param event the event
     */
    @FXML
    void changeName(MouseEvent event) {
        Guild currGuild = InGuildViewController.currGuild;
        String response = DiscordApplication.appController.changeGuildName(currGuild, nameTF.getText());
        System.out.println(response);
        InGuildViewController.currGuild = DiscordApplication.appController.getGuild(currGuild.getOwnerName(), nameTF.getText());
        cancel(event);
    }

}

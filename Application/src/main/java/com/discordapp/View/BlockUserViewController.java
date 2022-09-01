package com.discordapp.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The type Block user view controller.
 */
public class BlockUserViewController {

    private String username;

    /**
     * Initialize.
     *
     * @param username the username
     */
    @FXML
    public void initialize(String username){
        this.username = username;
    }

    /**
     * Block Friend
     */
    @FXML
    private void BlockFriend(MouseEvent event) {
        String response = DiscordApplication.appController.blockUser(username);
        System.out.println(response);
        cancel(event);
    }

    /**
     * close scene
     */
    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getOwner().getScene().getRoot();
        root.setEffect(null);
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
        DiscordApplication.loadNewScene(loader, stage);
    }

}

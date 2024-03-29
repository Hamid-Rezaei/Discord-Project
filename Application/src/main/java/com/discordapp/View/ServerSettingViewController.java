package com.discordapp.View;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * The type Server setting view controller.
 */
public class ServerSettingViewController {

    @FXML
    private void changeName(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        showPopUpStage(stage, "change-server-name-view.fxml");
    }

    @FXML
    private void createChannel(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        showPopUpStage(stage, "create-channel-view.fxml");
    }

    @FXML
    private void deleteServer(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        showPopUpStage(stage, "delete-server-view.fxml");
    }

    @FXML
    private void invitePeople(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        showPopUpStage(stage, "invite-people-view.fxml");
    }


    /**
     * Delete channel.
     *
     * @param event the event
     * @throws IOException the io exception
     */
    @FXML
    void deleteChannel(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        showPopUpStage(stage, "delete-channel-view.fxml");
    }


    /**
     * Show popup stage.
     */
    private void showPopUpStage(Stage stage,String fxml) throws IOException {
        stage.close();
        Stage popupStage = new Stage(StageStyle.TRANSPARENT);
        popupStage.initOwner(stage.getOwner());
        popupStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        popupStage.setScene(new Scene(loader.load(), Color.TRANSPARENT));
        popupStage.show();
    }
}

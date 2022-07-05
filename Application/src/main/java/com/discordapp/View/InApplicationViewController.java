package com.discordapp.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class InApplicationViewController {

    @FXML
    private Circle addServerIcon;
    @FXML
    private Circle avatar;
    @FXML
    private Circle discordIcon;
    @FXML
    private Label findFrd;
    @FXML
    private Label findFrdErr;
    @FXML
    private Circle settingIcon;
    @FXML
    private Circle status;

    @FXML
    public void initialize() {
        setAvatar();
        setDiscordIcon();
        setAddServerIcon();
        setSettingIcon();
        status.setFill(StatusViewController.color);
    }

    private void setDiscordIcon() {
        discordIcon.setStroke(Color.GRAY);
        discordIcon.setFill(new ImagePattern(new Image("file:assets/discord.png", false)));
    }

    private void setAvatar() {
        avatar.setStroke(Color.SEAGREEN);
        avatar.setFill(new ImagePattern(new Image(new ByteArrayInputStream(DiscordApplication.user.getAvatar()))));
    }

    private void setAddServerIcon() {
        addServerIcon.setFill(new ImagePattern(new Image("file:assets/add_server_icon.png", false)));
    }

    private void setSettingIcon() {
        settingIcon.setFill(new ImagePattern(new Image("file:assets/setting_icon.png", false)));
    }


    @FXML
    void changeStatus(MouseEvent event) {
        try {
            Stage popupStage = new Stage(StageStyle.TRANSPARENT);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            popupStage.initOwner(stage);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setY(event.getScreenY() - 270);
            popupStage.setX(event.getScreenX() + 20);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("status-view.fxml"));
            Parent root = loader.load();
            StatusViewController svc = loader.getController();
            svc.initialize(status);
            popupStage.setScene(new Scene(root, Color.TRANSPARENT));
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setStatus() {
        status.setFill(StatusViewController.color);
    }


    @FXML
    void addServer(MouseEvent event) {

    }

    @FXML
    void setting(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }

    @FXML
    void newDirectChatMessage(MouseEvent event) {

    }

}

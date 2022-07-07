package com.discordapp.View;

import com.discordapp.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AccountViewController {

    @FXML
    private Label emailLb;
    @FXML
    private Label phoneLb;
    @FXML
    private Label TitleNameLb;
    @FXML
    private Label usernameLb;
    @FXML
    private Circle avatar;
    @FXML
    private Circle status;

    public static User user;

    public static void setUser(User user) {
        AccountViewController.user = user;
    }

    @FXML
    public void initialize() {
        setUser(DiscordApplication.user);
        setAvatar();
        status.setFill(StatusViewController.color);
        TitleNameLb.setText(user.getUsername());
        usernameLb.setText(user.getUsername());
        emailLb.setText(user.getEmail());
        if (user.getPhoneNumber() != null && user.getPhoneNumber().length() > 1) {
            phoneLb.setText(user.getPhoneNumber());
        }
    }

    public void setAvatar() {
        if (avatar != null) {
            avatar.setStroke(Color.SEAGREEN);
            avatar.setFill(new ImagePattern(new Image(new ByteArrayInputStream(user.getAvatar()))));
        }
    }


    @FXML
    void changeAvatar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        //fileChooser.selectedExtensionFilterProperty(new FileChooser.ExtensionFilter("IMAGE FILES (*.png)","*.png"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {

            try {
                byte[] ava = new byte[(int) file.length()];
                FileInputStream f1 = new FileInputStream(file);
                f1.read(ava);
                f1.close();
                DiscordApplication.user.setAvatar(ava);
                avatar.setFill(new ImagePattern(new Image(new ByteArrayInputStream(DiscordApplication.user.getAvatar()))));
                DiscordApplication.appController.updateUser(DiscordApplication.user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void changePass(ActionEvent event) {

    }

    @FXML
    void editEmail(ActionEvent event) {

    }

    @FXML
    void editPhone(ActionEvent event) {

    }

    @FXML
    void editUsername(ActionEvent event) {

    }

    @FXML
    void escButton(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }

    @FXML
    void logOut(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.appController.disconnect();
        DiscordApplication.loadNewScene(loader, stage);
    }

}


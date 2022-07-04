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
import javafx.stage.Stage;

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
        setAvatar();
        setUser(DiscordApplication.user);
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
            avatar.setFill(new ImagePattern(new Image("file:assets/defaultAvatar.png", false)));
        }
    }

/*    @FXML
    void changeStatus(MouseEvent event) {
        if (event.isSecondaryButtonDown()) {
            Stage popupStage = new Stage(StageStyle.TRANSPARENT);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            popupStage.initOwner(stage);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("status-view.fxml"));
            try {
                popupStage.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            } catch (IOException e) {
                e.printStackTrace();
            }
            popupStage.show();
        }
        status.setFill(StatusViewController.color);
    }*/

    @FXML
    void changeAvatar(ActionEvent event) {

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

    }

}


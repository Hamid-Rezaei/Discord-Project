package com.discordapp.View;

import com.discordapp.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The type Account view controller.
 */
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

    /**
     * The constant user.
     */
    public static User user;

    /**
     * Sets user.
     *
     * @param user the user
     */
    public static void setUser(User user) {
        AccountViewController.user = user;
    }

    /**
     * Initialize.
     */
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

    /**
     * Sets avatar.
     */
    public void setAvatar() {
        if (avatar != null) {
            avatar.setStroke(Color.SEAGREEN);
            avatar.setFill(new ImagePattern(new Image(new ByteArrayInputStream(user.getAvatar()))));
        }
    }


    /**
     * Change avatar.
     *
     * @param event the event
     */
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

    /**
     * Change pass.
     *
     * @param event the event
     */
    @FXML
    void changePass(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        showPopupStage(stage, "chage-password-view.fxml");

    }

    /**
     * Edit email.
     *
     * @param event the event
     */
    @FXML
    void editEmail(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        showPopupStage(stage, "edit-email-view.fxml");
    }

    /**
     * Edit phone.
     *
     * @param event the event
     */
    @FXML
    void editPhone(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        showPopupStage(stage, "edit-phone-view.fxml");
    }

    /**
     * Edit username.
     *
     * @param event the event
     */
    @FXML
    void editUsername(ActionEvent event) {

    }

    /**
     * Esc button.
     *
     * @param event the event
     */
    @FXML
    void escButton(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }

    /**
     * Log out.
     *
     * @param event the event
     */
    @FXML
    void logOut(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.appController.disconnect();
        DiscordApplication.loadNewScene(loader, stage);
    }

    /**
     * show popup stage
     */
    private void showPopupStage(Stage stage, String fxml) {
        try {
            Parent root1 = stage.getScene().getRoot();
            ColorAdjust adj = new ColorAdjust(0, 0.1, -0.2, 0);
            GaussianBlur blur = new GaussianBlur(5);
            adj.setInput(blur);
            root1.setEffect(adj);

            Stage popupStage = new Stage(StageStyle.TRANSPARENT);
            popupStage.initOwner(stage);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            popupStage.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


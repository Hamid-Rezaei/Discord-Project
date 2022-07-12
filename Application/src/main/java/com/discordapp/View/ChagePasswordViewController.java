package com.discordapp.View;

import com.discordapp.Controller.Authentication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ChagePasswordViewController {

    @FXML
    private TextField passTF;
    @FXML
    private Label error;


    @FXML
    void cancel(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getOwner().getScene().getRoot();
        root.setEffect(null);
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account-view.fxml"));
        DiscordApplication.loadNewScene(loader, stage);
    }

    @FXML
    void changePassword(MouseEvent event) {
        String newPass = passTF.getText();

        if (Authentication.checkValidPass(newPass)) {
            DiscordApplication.user.setPassword(newPass);
            boolean updated = DiscordApplication.appController.updateUser(DiscordApplication.user);
            if (updated) {
                error.setText("Password changed successfully. please login again");
            } else {
                error.setText("couldn't change your password");
            }

        } else {
            error.setText("Oops, password is not valid");
        }
    }
}

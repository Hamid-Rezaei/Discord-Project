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

/**
 * The type Edit phone view controller.
 */
public class EditPhoneViewController {

    @FXML
    private TextField phone;

    @FXML
    private Label error;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account-view.fxml"));
        DiscordApplication.loadNewScene(loader, stage);
    }

    /**
     * Change phone.
     *
     * @param event the event
     */
    @FXML
    void changePhone(MouseEvent event) {
        String newPhone = phone.getText();
        if (Authentication.checkValidPhone(newPhone)) {
            DiscordApplication.user.setPhoneNumber(newPhone);
            boolean updated = DiscordApplication.appController.updateUser(DiscordApplication.user);
            if (updated) {
                error.setText("Phone number changed successfully. please login again");
            } else {
                error.setText("couldn't change your Phone number");
            }

        } else {
            error.setText("Oops, Phone number is not valid");
        }

    }

}

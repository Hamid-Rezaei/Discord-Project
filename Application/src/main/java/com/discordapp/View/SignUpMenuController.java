package com.discordapp.View;

import com.discordapp.Controller.AppController;
import com.discordapp.Controller.Authentication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SignUpMenuController {
    @FXML
    private TextField emailTF;
    @FXML
    private TextField passTF;
    @FXML
    private TextField usernameTF;
    @FXML
    private Label nameErr;
    @FXML
    private Label passErr;
    @FXML
    private Label emailErr;
    @FXML
    private Label resultLabel;

    private String email;
    private String password;
    private String username;
    private boolean canContinue = false;

    @FXML
    private Button continueBtn;

    @FXML
    public void initialize() {

    }

    @FXML
    void emailTFHandler(KeyEvent event) {
        emailErr.setText("");
        if (event.getCode() == KeyCode.ENTER) {
            email = emailTF.getText();
            if (!(email == null || email.equals(""))) {
                usernameTF.requestFocus();
            } else {
                emailErr.setText("THIS FIELD IS REQUIRED");
            }

        }
    }

    @FXML
    void passTFHandler(KeyEvent event) {
        passErr.setText("");
        if (event.getCode() == KeyCode.ENTER) {
            password = passTF.getText();
            if (!(password == null || password.equals(""))) {
                continueBtn.fire();
            } else {
                emailErr.setText("THIS FIELD IS REQUIRED");
            }

        }
    }

    @FXML
    void UsernameTFHandler(KeyEvent event) {
        nameErr.setText("");
        if (event.getCode() == KeyCode.ENTER) {
            username = usernameTF.getText();
            if (!(username == null || username.equals(""))) {
                passTF.requestFocus();
            } else {
                nameErr.setText("THIS FIELD IS REQUIRED");
            }
        }
    }

    public void checkValidation() {
        String result = Authentication.checkValidationOfInfo(username, password, email);
        if (result.startsWith("Success")) {
            canContinue = true;
        } else if (result.startsWith("EMAIL")) {
            emailErr.setText(result);
        } else if (result.startsWith("PASSWORD")) {
            passErr.setText(result);
        } else if (result.startsWith("USERNAME")) nameErr.setText(result);
    }


    @FXML
    void continueButton(ActionEvent event) {
        username = usernameTF.getText();
        email = emailTF.getText();
        password = passTF.getText();
        checkValidation();
        if (canContinue) {
            DiscordApplication.appController = new AppController();
            DiscordApplication.appController.connect();
            String result = DiscordApplication.appController.signUp(username, password, email);
            if(result.equals("Success.")){
                resultLabel.setTextFill(Color.GREEN);
                resultLabel.setText("Successfully signed up. please login");
            } else {
                resultLabel.setTextFill(Color.RED);
                resultLabel.setText(result);
            }
            //TODO: sign up!
        }
    }

    @FXML
    void goToLogin(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }

}
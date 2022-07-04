package com.discordapp.View;

import com.discordapp.Controller.Authentication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginViewController {

    @FXML
    private TextField emPhUserTF;
    @FXML
    private TextField passTF;
    @FXML
    private Label emailPhoneErr;
    @FXML
    private Label passErr;

    private String password;
    private String email;
    private String phoneNum;
    public String username;

    @FXML
    public void initialize() {
    }

    @FXML
    void emPhUserTFHandler(KeyEvent event) {
        emailPhoneErr.setText("");
        if (event.getCode() == KeyCode.ENTER) {
            passTF.requestFocus();
            String text = emPhUserTF.getText();
            username = text;
            String type = Authentication.checkWhichOne(text);
            switch (type) {
                case "USERNAME" -> username = text;
                case "EMAIL" -> email = text;
                default -> phoneNum = text;
            }
        }
    }


    @FXML
    void passTFHandler(KeyEvent event) {
        passErr.setText("");
        if (event.getCode() == KeyCode.ENTER) {
            password = passTF.getText();
        }
    }

    @FXML
    void onLoginButton(ActionEvent event) {
        String text = emPhUserTF.getText();
        String type = Authentication.checkWhichOne(text);
        switch (type) {
            case "USERNAME" -> username = text;
            case "EMAIL" -> email = text;
            default -> phoneNum = text;
        }
        password = passTF.getText();
/*        if(password == null || password.equals("")){
            passErr.setText("PASSWORD-This field is required");
        }else if((phoneNum == null && phoneNum.equals("")) && (username == null && email == null)){
            emailPhoneErr.setText("EMAIL OR PHONE NUMBER OR USERNAME-This field is required");
        }*/
        //TODO:dooooooooooooooo
        DiscordApplication.user = DiscordApplication.appController.login(username, password);
        AccountViewController.setUser(DiscordApplication.user);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }

    @FXML
    void goToSignUp(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signup-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }
}

package application.view;

import application.controller.Authentication;
import javafx.application.Application;
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

    private String email;
    private String password;
    private String username;
    private boolean canContinue = false;

    @FXML
    public void initialize() {
    }

    @FXML
    void emailTFHandler(KeyEvent event) {
        emailErr.setText("");
        if (event.getCode() == KeyCode.ENTER) {
            passTF.requestFocus();
            email = emailTF.getText();
            checkValidation();
        }
    }

    @FXML
    void passTFHandler(KeyEvent event) {
        passErr.setText("");
        if (event.getCode() == KeyCode.ENTER) {
            usernameTF.requestFocus();
            password = passTF.getText();
            checkValidation();
        }
    }

    @FXML
    void UsernameTFHandler(KeyEvent event) {
        nameErr.setText("");
        if (event.getCode() == KeyCode.ENTER) {
            username = usernameTF.getText();
            checkValidation();
        }
    }

    public void checkValidation() {
        String result = Authentication.checkValidationOfInfo(username, password, email);
        if (result.startsWith("Success")) {
            canContinue = true;
        } else if (result.startsWith("EMAIL")) {
            emailErr.setText(result);
        }else if (result.startsWith("PASSWORD")){
            passErr.setText(result);
        }else if(result.startsWith("USERNAME")){
            nameErr.setText(result);
        }
    }


    @FXML
    void continueButton(ActionEvent event) {
        checkValidation();
        if(canContinue){
            System.out.println("successfully sing up");
            //TODO: fffffffffff
        }
    }

    @FXML
    void goToLogin(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }

}
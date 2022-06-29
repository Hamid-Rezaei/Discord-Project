package application.view;

import application.controller.Authentication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
    private String username;

    @FXML
    public void initialize() {
    }

    @FXML
    void emPhUserTFHandler(KeyEvent event) {
        emailPhoneErr.setText("");
        if (event.getCode() == KeyCode.ENTER) {
            passTF.requestFocus();
            String text = emPhUserTF.getText();
            String type = Authentication.checkWhichOne(text);
            switch (type){
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
        if(password == null){
            passErr.setText("PASSWORD-This field is required");
        }else if(phoneNum == null && username == null && email == null){
            emailPhoneErr.setText("EMAIL OR PHONE NUMBER OR USERNAME-This field is required");
        }
        //TODO:dooooooooooooooo
    }
}

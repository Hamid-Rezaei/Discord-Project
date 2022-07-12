package com.discordapp.Controller;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.discordapp.Controller.Handler.matchedInput;

abstract class Handler {
    protected Handler nextHandler;

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }


    public static boolean matchedInput(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }


    public abstract String handle(String username, String password, String email);
}


class UsernameHandler extends Handler {

    @Override
    public String handle(String username, String password, String email) {
        String usernameRegex = "^[A-Za-z0-9]{6,}$";
        if (username == null || username.equals("")) {
            return "USERNAME-This field is required";
        } else if (!matchedInput(usernameRegex, username)) {
            return "USERNAME-Username length must be at least 6 and contains numbers and characters.";
        } else {
            if (nextHandler != null)
                return nextHandler.handle(username, password, email);
            return "Success";
        }
    }
}


class PasswordHandler extends Handler {

    @Override
    public String handle(String username, String password, String email) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d+]{8,}$";
        if (password == null || password.equals("")) {
            return "PASSWORD-This field is required";
        } else if (!matchedInput(passwordRegex, password)) {
            return "PASSWORD-Password length must be at least 8 and contains numbers and characters.";
        } else {
            if (nextHandler != null)
                return nextHandler.handle(username, password, email);
            return "Success";
        }
    }
}


class EmailHandler extends Handler {

    @Override
    public String handle(String username, String password, String email) {
        String emailRegex = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z]+(?:\\.[a-zA-Z]+)*$";
        if (email == null || email.equals("")) {
            return "EMAIL-This field is required";
        } else if (!matchedInput(emailRegex, email)) {
            return "EMAIL-Provided email wasn't valid.";
        } else {
            if (nextHandler != null)
                return nextHandler.handle(username, password, email);
            return "Success";
        }
    }
}


public class Authentication {
    public static String checkValidationOfInfo(String username, String password, String email) {
        EmailHandler emailHandler = new EmailHandler();
        UsernameHandler usernameHandler = new UsernameHandler();
        PasswordHandler passwordHandler = new PasswordHandler();

        emailHandler.setNextHandler(passwordHandler);
        passwordHandler.setNextHandler(usernameHandler);

        return emailHandler.handle(username, password, email);
    }

    public static boolean checkValidPass(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d+]{8,}$";
        return matchedInput(passwordRegex, password);
    }

    public static boolean checkValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z]+(?:\\.[a-zA-Z]+)*$";
        return matchedInput(emailRegex, email);
    }
}

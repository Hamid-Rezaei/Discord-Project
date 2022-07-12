package com.discordapp.View;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class ChatController {

    @FXML
    private Circle paperclip;


    public void initialize() {
        paperclip.setFill(new ImagePattern(new Image("file:assets/plus.png")));
    }


    @FXML
    void sendMessage(KeyEvent event) {

    }

}

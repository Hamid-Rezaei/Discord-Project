package com.discordapp.View;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


public class AnimationViewController {
    @FXML
    private ImageView gif;

    @FXML
    public void initialize() {
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(gif);
        rotate.setDuration(Duration.seconds(5));
        rotate.setCycleCount(TranslateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setByAngle(360);
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.play();
    }

}

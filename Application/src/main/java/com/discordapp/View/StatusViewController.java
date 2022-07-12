package com.discordapp.View;

import com.discordapp.Model.Status;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValueBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * The type Status view controller.
 */
public class StatusViewController {
    /**
     * The constant color.
     */
    public static Color color = Color.GREEN;
    /**
     * The Circle.
     */
    public Circle circle;
    /**
     * The Status.
     */
    Status status = Status.INVISIBLE;

    /**
     * Return color color.
     *
     * @param status the status
     * @return the color
     */
    public static Color returnColor(String status) {
        switch (status) {
            case "offline" -> {
                return Color.GRAY;
            }
            case "online" -> {
                return Color.GREEN;
            }
            case "idle" -> {
                return Color.YELLOW;
            }
            case "do not disturb" -> {
                return Color.RED;
            }

        }
        return Color.WHITE;
    }

    /**
     * Initialize.
     *
     * @param circle the circle
     */
    @FXML
    public void initialize(Circle circle) {
        this.circle = circle;
        circle.setFill(color);
        status = Status.ONLINE;
    }

    /**
     * Disturb.
     *
     * @param event the event
     */
    @FXML
    void disturb(MouseEvent event) {
        color = Color.RED;
        status = Status.DO_NOT_DISTURB;
        closePopupStage(event);
    }

    /**
     * Idle.
     *
     * @param event the event
     */
    @FXML
    void idle(MouseEvent event) {
        color = Color.YELLOW;
        status = Status.IDLE;
        closePopupStage(event);
    }

    /**
     * Invisible.
     *
     * @param event the event
     */
    @FXML
    void invisible(MouseEvent event) {
        color = Color.GRAY;
        status = Status.INVISIBLE;
        closePopupStage(event);
    }

    /**
     * Online.
     *
     * @param event the event
     */
    @FXML
    void online(MouseEvent event) {
        color = Color.GREEN;
        status = Status.ONLINE;
        closePopupStage(event);
    }

    /**
     * Close popup stage.
     *
     * @param event the event
     */
    @FXML
    public void closePopupStage(MouseEvent event) {
        circle.setFill(color);
        DiscordApplication.appController.setStatus(status.toString(status), DiscordApplication.user.getUsername());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}

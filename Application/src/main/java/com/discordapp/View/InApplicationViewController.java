package com.discordapp.View;

import com.discordapp.Controller.AppController;
import com.discordapp.Model.User;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class InApplicationViewController {

    @FXML
    private Circle addServerIcon;

    @FXML
    private Circle avatar;

    @FXML
    private Circle discordIcon;

    @FXML
    private TextField friendIDTF;

    @FXML
    private Button friendReqBtn;

    @FXML
    private Label frndReqResponse;

    @FXML
    private Circle settingIcon;

    @FXML
    private Circle status;

    @FXML
    private ListView<User> friendsList;
    @FXML
    private ListView<User> onlineList;
    @FXML
    private ListView<User> blockList;
    @FXML
    private ListView<User> pendingList;

    public void initialize() {
        setAvatar();
        setDiscordIcon();
        setAddServerIcon();
        setSettingIcon();
        status.setFill(StatusViewController.color);
    }

    private void setDiscordIcon() {
        discordIcon.setStroke(Color.GRAY);
        discordIcon.setFill(new ImagePattern(new Image("file:assets/discord.png", false)));
    }

    private void setAvatar() {
        avatar.setStroke(Color.SEAGREEN);
        avatar.setFill(new ImagePattern(new Image(new ByteArrayInputStream(DiscordApplication.user.getAvatar()))));
    }

    private void setAddServerIcon() {
        addServerIcon.setFill(new ImagePattern(new Image("file:assets/add_server_icon.png", false)));
    }

    private void setSettingIcon() {
        settingIcon.setFill(new ImagePattern(new Image("file:assets/setting_icon.png", false)));
    }

    @FXML
    void sendFriendReq(ActionEvent event) {
        String friendName = friendIDTF.getText();
        String respond = DiscordApplication.appController.friendRequest(DiscordApplication.user.getUsername(), friendName);
        if (respond.equals("Success.")) {
            frndReqResponse.setTextFill(Color.GREEN);
            frndReqResponse.setText("Friend request sent.");
        } else {
            frndReqResponse.setTextFill(Color.RED);
            frndReqResponse.setText(respond);
        }
    }

    @FXML
    void sendFrReq(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            friendReqBtn.fire();
        }
    }

    @FXML
    void changeStatus(MouseEvent event) {
        try {
            Stage popupStage = new Stage(StageStyle.TRANSPARENT);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            popupStage.initOwner(stage);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setY(event.getScreenY() - 270);
            popupStage.setX(event.getScreenX() + 20);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("status-view.fxml"));
            Parent root = loader.load();
            StatusViewController svc = loader.getController();
            svc.initialize(status);
            popupStage.setScene(new Scene(root, Color.TRANSPARENT));
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setStatus() {
        status.setFill(StatusViewController.color);
    }


    @FXML
    void addServer(MouseEvent event) {

    }

    @FXML
    void showAllFriends() {
        ObservableList<User> friendObs = FXCollections.observableArrayList(DiscordApplication.appController.friendList(DiscordApplication.user.getUsername()));
        friendsList.setItems(friendObs);
        friendsList.setCellFactory(param -> new ListCell<>() {
            TextField nameTF = new TextField();
            Image profile;
            Circle circle = new Circle();
            User user;

            @Override
            public void updateItem(User usr, boolean empty) {
                super.updateItem(user, empty);
                setText(null);
                setGraphic(null);
                if (usr != null && !empty) {
                    nameTF.setText(usr.getUsername());
                    nameTF.setEditable(false);
                    nameTF.setStyle("-fx-background-color: #36393f;" +
                            "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold");

                    Circle status = new Circle();
                    status.setCenterX(circle.getCenterX() + 51);
                    status.setCenterY(circle.getCenterY() + 51);

                    status.setFill(StatusViewController.returnColor(usr.getStatus().toString(usr.getStatus())));

                    status.setRadius(8);

                    status.setManaged(false);
                    user = usr;
                    profile = new Image(new ByteArrayInputStream(user.getAvatar()));
                    circle.setFill(new ImagePattern(profile));
                    circle.setRadius(30);
                    HBox hBox = new HBox();
                    Pane pane = new Pane();
                    Line line = new Line();
                    line.setStroke(new Color(0.302, 0.302, 0.302, 1.0));
                    line.setManaged(false);
                    line.setStartX(-151);
                    line.setEndX(1400);
                    line.setScaleY(3);
                    line.setScaleZ(2);
                    line.setSmooth(true);
                    hBox.getChildren().addAll(circle, status, nameTF, line, pane);
                    pane.setStyle("-fx-background-color: #36393f;");
                    hBox.setHgrow(pane, Priority.ALWAYS);
                    hBox.setHgrow(line, Priority.ALWAYS);
                    line.setLayoutX(hBox.getLayoutX() + 150);
                    line.setLayoutY(hBox.getLayoutY() + 75);
                    setGraphic(hBox);
                    setStyle("-fx-background-color: #36393f;" + "-fx-padding: 35px;");
                }
                setStyle("-fx-background-color: #36393f;" + "-fx-padding: 15px;");
            }
        });
    }

    @FXML
    void showOnlineFriends() {
        ArrayList<User> friends = DiscordApplication.appController.friends(DiscordApplication.user.getUsername());
        friends.removeIf(param -> param.getStatus().toString(param.getStatus()).equals("offline"));
        ObservableList<User> onlineObs = FXCollections.observableArrayList(friends);
        onlineList.setItems(onlineObs);
        onlineList.setCellFactory(param -> new ListCell<>() {
            TextField nameTF = new TextField();
            Image profile;
            Circle circle = new Circle();
            User user;

            @Override
            public void updateItem(User usr, boolean empty) {
                super.updateItem(user, empty);
                setText(null);
                setGraphic(null);
                if (usr != null && !empty) {
                    nameTF.setText(usr.getUsername());
                    nameTF.setEditable(false);
                    nameTF.setStyle("-fx-background-color: #36393f;" +
                            "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold");

                    Circle status = new Circle();
                    status.setCenterX(circle.getCenterX() + 51);
                    status.setCenterY(circle.getCenterY() + 51);

                    status.setFill(StatusViewController.returnColor(usr.getStatus().toString(usr.getStatus())));

                    status.setRadius(8);

                    status.setManaged(false);
                    user = usr;
                    profile = new Image(new ByteArrayInputStream(user.getAvatar()));
                    circle.setFill(new ImagePattern(profile));
                    circle.setRadius(30);
                    HBox hBox = new HBox();
                    Pane pane = new Pane();
                    Line line = new Line();
                    line.setStroke(new Color(0.302, 0.302, 0.302, 1.0));
                    line.setManaged(false);
                    line.setStartX(-151);
                    line.setEndX(1400);
                    line.setScaleY(3);
                    line.setScaleZ(2);
                    line.setSmooth(true);
                    hBox.getChildren().addAll(circle, status, nameTF, line, pane);
                    pane.setStyle("-fx-background-color: #36393f;");
                    hBox.setHgrow(pane, Priority.ALWAYS);
                    hBox.setHgrow(line, Priority.ALWAYS);
                    line.setLayoutX(hBox.getLayoutX() + 150);
                    line.setLayoutY(hBox.getLayoutY() + 75);
                    setGraphic(hBox);
                    setStyle("-fx-background-color: #36393f;" + "-fx-padding: 35px;");
                }
                setStyle("-fx-background-color: #36393f;" + "-fx-padding: 15px;");
            }
        });
    }

    @FXML
    void showPendingRequests() {
        ArrayList<User> pendingReqs = DiscordApplication.appController.friendRequestList(DiscordApplication.user.getUsername());
        ObservableList<User> pendingObs = FXCollections.observableArrayList(pendingReqs);
        pendingList.setItems(pendingObs);
        pendingList.setCellFactory(param -> new ListCell<>() {
            TextField nameTF = new TextField();
            Image profile;
            Circle circle = new Circle();
            User user;
            Button accept;
            Button reject;
            @Override
            public void updateItem(User usr, boolean empty) {
                super.updateItem(user, empty);
                setText(null);
                setGraphic(null);
                if (usr != null && !empty) {
                    nameTF.setText(usr.getUsername());
                    nameTF.setEditable(false);
                    nameTF.setStyle("-fx-background-color: #36393f;" +
                            "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold");

                    Circle status = new Circle();
                    status.setCenterX(circle.getCenterX() + 51);
                    status.setCenterY(circle.getCenterY() + 51);
                    accept = new Button();
                    reject = new Button();
                    accept.setText("ACCEPT");
                    reject.setText("REJECT");

                    status.setFill(StatusViewController.returnColor(usr.getStatus().toString(usr.getStatus())));

                    status.setRadius(8);

                    status.setManaged(false);
                    user = usr;
                    profile = new Image(new ByteArrayInputStream(user.getAvatar()));
                    circle.setFill(new ImagePattern(profile));
                    circle.setRadius(30);
                    HBox hBox = new HBox();
                    Pane pane = new Pane();
                    Line line = new Line();
                    line.setStroke(new Color(0.302, 0.302, 0.302, 1.0));
                    line.setManaged(false);
                    line.setStartX(-151);
                    line.setEndX(1400);
                    line.setScaleY(3);
                    line.setScaleZ(2);
                    line.setSmooth(true);
                    hBox.getChildren().addAll(circle, status, nameTF, line, pane,accept,reject);
                    pane.setStyle("-fx-background-color: #36393f;");
                    hBox.setHgrow(pane, Priority.ALWAYS);
                    hBox.setHgrow(line, Priority.ALWAYS);
                    line.setLayoutX(hBox.getLayoutX() + 150);
                    line.setLayoutY(hBox.getLayoutY() + 75);
                    setGraphic(hBox);
                    setStyle("-fx-background-color: #36393f;" + "-fx-padding: 35px;");
                }
                setStyle("-fx-background-color: #36393f;" + "-fx-padding: 15px;");
            }
        });
    }

    @FXML
    void showBlockedList() {
        ArrayList<User> blocked = DiscordApplication.appController.blockedList(DiscordApplication.user.getUsername());
        ObservableList<User> blockedObs = FXCollections.observableArrayList(blocked);
        blockList.setItems(blockedObs);
        blockList.setCellFactory(param -> new ListCell<>() {
            TextField nameTF = new TextField();
            Image profile;
            Circle circle = new Circle();
            User user;

            @Override
            public void updateItem(User usr, boolean empty) {
                super.updateItem(user, empty);
                setText(null);
                setGraphic(null);
                if (usr != null && !empty) {
                    nameTF.setText(usr.getUsername());
                    nameTF.setEditable(false);
                    nameTF.setStyle("-fx-background-color: #36393f;" +
                            "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold");

                    Circle status = new Circle();
                    status.setCenterX(circle.getCenterX() + 51);
                    status.setCenterY(circle.getCenterY() + 51);

                    status.setFill(StatusViewController.returnColor(usr.getStatus().toString(usr.getStatus())));

                    status.setRadius(8);

                    status.setManaged(false);
                    user = usr;
                    profile = new Image(new ByteArrayInputStream(user.getAvatar()));
                    circle.setFill(new ImagePattern(profile));
                    circle.setRadius(30);
                    HBox hBox = new HBox();
                    Pane pane = new Pane();
                    Line line = new Line();
                    line.setStroke(new Color(0.302, 0.302, 0.302, 1.0));
                    line.setManaged(false);
                    line.setStartX(-151);
                    line.setEndX(1400);
                    line.setScaleY(3);
                    line.setScaleZ(2);
                    line.setSmooth(true);
                    hBox.getChildren().addAll(circle, status, nameTF, line, pane);
                    pane.setStyle("-fx-background-color: #36393f;");
                    hBox.setHgrow(pane, Priority.ALWAYS);
                    hBox.setHgrow(line, Priority.ALWAYS);
                    line.setLayoutX(hBox.getLayoutX() + 150);
                    line.setLayoutY(hBox.getLayoutY() + 75);
                    setGraphic(hBox);
                    setStyle("-fx-background-color: #36393f;" + "-fx-padding: 35px;");
                }
                setStyle("-fx-background-color: #36393f;" + "-fx-padding: 15px;");
            }
        });
    }

    @FXML
    void allFriends() {
        showAllFriends();
    }


    @FXML
    void onlineFriends() {
        showOnlineFriends();
    }


    @FXML
    void pendingRequests() {
        showPendingRequests();
    }

    @FXML
    void blockedList() {
        showBlockedList();
    }


    @FXML
    void setting(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }

    @FXML
    void newDirectChatMessage(MouseEvent event) {

    }

}

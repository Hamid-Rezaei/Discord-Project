package com.discordapp.View;

import com.discordapp.Model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The type In application view controller.
 */
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
    @FXML
    private ListView<Guild> guildList;
    @FXML
    private ComboBox<User> directMessages;
    @FXML
    private ListView<Message> messageList;
    @FXML
    private AnchorPane friendTabPane;
    @FXML
    private BorderPane inAppPane;
    @FXML
    private TextField messageTF;

    @FXML
    private ComboBox<Message> pinBox;

    @FXML
    private Circle pinIcon;


    /**
     * The Friend in chat.
     */
    User friendInChat;
    /**
     * The Listen to msg.
     */
    Thread listenToMsg;

    /**
     * Initialize.
     */
    public void initialize() {
        setAvatar();
        setDiscordIcon();
        setAddServerIcon();
        setSettingIcon();
        showGuilds();
        setStatus();
        setPinIcon();
        showAllFriends();
    }

    private void setPinIcon() {
        pinIcon.setFill(new ImagePattern(new Image("file:assets/pin.png")));
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

    /**
     * Send friend req.
     *
     * @param event the event
     */
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

    /**
     * Send fr req.
     *
     * @param event the event
     */
    @FXML
    void sendFrReq(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            friendReqBtn.fire();
        }
    }

    /**
     * Change status.
     *
     * @param event the event
     */
    @FXML
    void changeStatus(MouseEvent event) {
        if (listenToMsg == null || !listenToMsg.isAlive())
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

    /**
     * Sets status.
     */
    public void setStatus() {
        status.setFill(StatusViewController.color);
    }


    /**
     * Add server.
     *
     * @param event the event
     */
    @FXML
    void addServer(MouseEvent event) {
        exitFromDirectChat();
        try {
            Stage popupStage = new Stage(StageStyle.TRANSPARENT);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            popupStage.initOwner(stage);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setY(event.getScreenY() - 350);
            popupStage.setX(event.getScreenX() + 20);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-server-view.fxml"));
            popupStage.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void showGuilds() {
        ObservableList<Guild> guilds = FXCollections.observableArrayList(DiscordApplication.appController.listOfJoinedServers(DiscordApplication.user.getUsername()));
        guildList.setItems(guilds);
        guildList.setCellFactory(param -> new ListCell<>() {

            @Override
            public void updateItem(Guild guild, boolean empty) {
                super.updateItem(guild, empty);
                setText(null);
                setGraphic(null);
                if (guild != null && !empty) {
                    setEditable(false);
                    setText(guild.getName());
                    setOnMouseClicked(event -> {
                        InGuildViewController.currGuild = guildList.getSelectionModel().getSelectedItem();
                        exitFromDirectChat();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-guild-view.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        DiscordApplication.loadNewScene(loader, stage);
                    });

                }
                setStyle("-fx-background-color: #202225;" + "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-padding: 15px;");
            }
        });

    }

    /**
     * Show all friends.
     */
    @FXML
    void showAllFriends() {
        ObservableList<User> friendObs = FXCollections.observableArrayList(DiscordApplication.appController.friendList(DiscordApplication.user.getUsername()));

        friendsList.setItems(friendObs);
        friendsList.setCellFactory(param -> new ListCell<>() {
            Label nameTF = new Label();
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

                    param.setOnMouseReleased(new EventHandler<>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                                try {
                                    Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                                    Parent root1 = stage.getScene().getRoot();
                                    ColorAdjust adj = new ColorAdjust(0, 0.1, -0.2, 0);
                                    GaussianBlur blur = new GaussianBlur(5);
                                    adj.setInput(blur);
                                    root1.setEffect(adj);

                                    Stage popupStage = new Stage(StageStyle.TRANSPARENT);
                                    popupStage.initOwner(stage);
                                    popupStage.initModality(Modality.APPLICATION_MODAL);
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("block-user-view.fxml"));
                                    Parent root = loader.load();
                                    BlockUserViewController bUc = loader.getController();
                                    bUc.initialize(getItem().getUsername());
                                    popupStage.setScene(new Scene(root, Color.TRANSPARENT));
                                    popupStage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                }
                setStyle("-fx-background-color: #36393f;" + "-fx-padding: 15px;");
            }
        });
        newDirectChat(new ActionEvent());
    }

    /**
     * Show online friends.
     */
    @FXML
    void showOnlineFriends() {
        ArrayList<User> friends = DiscordApplication.appController.friends(DiscordApplication.user.getUsername());
        ArrayList<User> onlineFriends = new ArrayList<>();
        if(friends.size() > 0) {
            for(User friend : friends){
                if(friend.getStatus().toString(friend.getStatus()).equals("online")){
                    onlineFriends.add(friend);
                }
            }
            //friends.removeIf(param -> param.getStatus().toString(param.getStatus()).equals("offline"));
        }
        ObservableList<User> onlineObs = FXCollections.observableArrayList(onlineFriends);
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

    /**
     * Show pending requests.
     */
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
                    accept.setStyle("-fx-background-color: #0fc90c;" + " -fx-text-fill: #ffffff");
                    reject.setStyle("-fx-background-color: #c90c0c;" + " -fx-text-fill: #ffffff");
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
                    hBox.getChildren().addAll(circle, status, nameTF, line, pane, accept, reject);
                    pane.setStyle("-fx-background-color: #36393f;");
                    hBox.setHgrow(pane, Priority.ALWAYS);
                    hBox.setHgrow(line, Priority.ALWAYS);
                    line.setLayoutX(hBox.getLayoutX() + 150);
                    line.setLayoutY(hBox.getLayoutY() + 75);
                    setGraphic(hBox);
                    accept.setOnAction(e -> {
                        String username = DiscordApplication.user.getUsername();
                        HashSet<String> friend = new HashSet<>();
                        friend.add(user.getUsername());
                        DiscordApplication.appController.revisedFriendRequests(username, friend, new HashSet<String>());
                        Platform.runLater(() -> pendingList.getItems().remove(this.getIndex()));
                        e.consume();
                    });
                    reject.setOnAction(e -> {
                        String username = DiscordApplication.user.getUsername();
                        HashSet<String> reject = new HashSet<>();
                        reject.add(user.getUsername());
                        DiscordApplication.appController.revisedFriendRequests(username, new HashSet<>(), reject);
                        Platform.runLater(() -> pendingList.getItems().remove(this.getIndex()));
                        e.consume();

                    });
                    setStyle("-fx-background-color: #36393f;" + "-fx-padding: 35px;");
                }
                setStyle("-fx-background-color: #36393f;" + "-fx-padding: 15px;");
            }
        });
    }

    /**
     * Show blocked list.
     */
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
            Button unBlockBtn;

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
                    unBlockBtn = new Button();
                    unBlockBtn.setText("UNBLOCK");
                    unBlockBtn.setStyle("-fx-background-color: #ff0000;" + "-fx-text-fill: #ffff");
                    unBlockBtn.setOnMouseReleased(e -> {
                        DiscordApplication.appController.unblockUser(getItem().getUsername());
                        Platform.runLater(() ->
                                blockList.getItems().remove(getItem()));
                    });
                    hBox.getChildren().addAll(circle, status, nameTF, line, pane, unBlockBtn);
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

    /**
     * All friends.
     */
    @FXML
    void allFriends() {
        showAllFriends();
    }


    /**
     * Online friends.
     */
    @FXML
    void onlineFriends() {
        showOnlineFriends();
    }


    /**
     * Pending requests.
     */
    @FXML
    void pendingRequests() {
        showPendingRequests();
    }

    /**
     * Blocked list.
     */
    @FXML
    void blockedList() {
        showBlockedList();
    }


    /**
     * Sets .
     *
     * @param event the event
     */
    @FXML
    void setting(MouseEvent event) {
        exitFromDirectChat();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }

    /**
     * New direct chat.
     *
     * @param event the event
     */
    @FXML
    void newDirectChat(ActionEvent event) {
        ObservableList<User> friendObs = FXCollections.observableArrayList(DiscordApplication.appController.friendList(DiscordApplication.user.getUsername()));
        directMessages.setItems(friendObs);
        directMessages.setCellFactory(param -> {

            ListCell<User> cell = new ListCell<>() {
                @Override
                public void updateItem(User usr, boolean empty) {
                    super.updateItem(usr, empty);
                    setText(null);
                    setGraphic(null);
                    if (usr != null && !empty) {
                        setText(usr.getUsername());
                        setStyle("-fx-background-color: #36393f;" +
                                "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold");
                        setOnMouseClicked(e -> {
                            goToDirectChat(getItem());
                        });
                    } else {
                        setStyle("-fx-background-color: #36393f;" + "-fx-padding: 15px;");
                    }
                }
            };
            cell.setOnMouseReleased(e -> {
                if (!cell.isEmpty()) {
                    goToDirectChat(cell.getItem());
                    e.consume();
                }
            });
            return cell;
        });
    }

    /**
     * Go toin app.
     *
     * @param event the event
     */
    @FXML
    void goToinApp(MouseEvent event) {
        exitFromDirectChat();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        TabPane friendPane = (TabPane) friendTabPane.getChildren().get(0);
        friendPane.setVisible(true);
        friendPane.setDisable(false);
        AnchorPane directChat = (AnchorPane) friendTabPane.getChildren().get(1);
        directChat.setVisible(false);
        directChat.setDisable(true);
        DiscordApplication.loadNewScene(loader, stage);
    }

    /**
     * Go to direct chat.
     *
     * @param user the user
     */
    void goToDirectChat(User user) {

        if (!exitFromDirectChat()) {
            TabPane friendPane = (TabPane) friendTabPane.getChildren().get(0);
            friendPane.setVisible(false);
            friendPane.setDisable(true);
            AnchorPane directChat = (AnchorPane) friendTabPane.getChildren().get(1);
            Circle circle = (Circle) directChat.getChildren().get(2);
            circle.setFill(new ImagePattern(new Image("file:assets/plus.png")));
            directChat.setVisible(true);
            directChat.setDisable(false);
            friendInChat = user;
            loadDirectChatMessages();
            showPinnedList();
            listenToMsg = new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message("", "", LocalDateTime.now());
                    while (!message.getContent().equals("you exited the chat")) {
                        try {
                            Object obj = DiscordApplication.appController.getInputStream().readObject();
                            if (obj instanceof Message) {
                                message = (Message) obj;
                                addMessage(message);
                            } else {
                                if (obj.toString().equals("you exited the chat")) {
                                    break;
                                } else {
                                    System.out.println(obj);
                                }
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            });
            listenToMsg.start();
        }
    }

    /**
     * Add message.
     *
     * @param incomingMessage the incoming message
     */
    public void addMessage(Message incomingMessage) {
        Platform.runLater(() -> {
            messageList.getItems().add(incomingMessage);
        });
    }

    private void loadDirectChatMessages() {
        ArrayList<Message> msgs = DiscordApplication.appController.loadDirectChatMessages(DiscordApplication.user.getUsername(), friendInChat.getUsername());
        ObservableList<Message> obsMessage = FXCollections.observableArrayList(msgs);
        messageList.setItems(obsMessage);
        messageList.setCellFactory(e -> new ListCell<>() {
            @Override
            protected void updateItem(Message message, boolean b) {
                super.updateItem(message, b);
                setText(null);
                if (message != null && !b) {
                    Label messageContent = new Label();
                    if (!message.isFile()) {
                        if (!message.getContent().startsWith("#react")) {
                            messageContent.setText(message.toString());
                            messageContent.setStyle("-fx-background-color: #36393f;" + "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 12;" + "-fx-font-weight: bold;" + "-fx-padding: 5px");
                        }
                    } else {
                        setCursor(Cursor.HAND);
                        messageContent.setText(message.fileToString());
                        messageContent.setStyle("-fx-background-color: #36393f;" + "-fx-text-fill: rgba(7,141,242,0.89) ;" + "-fx-font-size: 12;" + "-fx-font-weight: bold;" + "-fx-padding: 5px");
                        messageContent.setOnMouseReleased(e -> {
                            saveFileInDownloads(message);
                        });
                    }
                    HBox hBox = new HBox();
                    hBox.setPrefSize(150, 25);
                    hBox.setAlignment(Pos.BASELINE_LEFT);


                    Rectangle likeR = new Rectangle(25, 25);
                    likeR.setFill(new ImagePattern(new Image("file:assets/like.png", false)));
                    Label likeCount = new Label();
                    likeCount.setPrefSize(25, 25);
                    likeCount.setStyle("-fx-font-size: 15;" + "-fx-text-fill: white;");

//                    likeR.setLayoutX(getLayoutX() + 100);
//                    likeR.setLayoutY(getLayoutY());
                    int likeCountInt = message.getReactCount("like");
                    //System.out.println(likeCountInt);
                    if (likeCountInt > 0) {
                        likeR.setVisible(true);
                        likeCount.setText(String.valueOf(likeCountInt));
                    } else {
                        likeR.setVisible(false);
                    }

                    Rectangle disLikeR = new Rectangle(25, 25);
                    disLikeR.setFill(new ImagePattern(new Image("file:assets/dislike.png", false)));
                    Label disLikeCount = new Label();
                    disLikeCount.setPrefSize(25, 25);
                    disLikeCount.setStyle("-fx-font-size: 15;" + "-fx-text-fill: white;");
                    int disLikeCountInt = message.getReactCount("dislike");
                    if (disLikeCountInt > 0) {
                        disLikeR.setVisible(true);
                        disLikeCount.setText(String.valueOf(disLikeCountInt));
                    } else {
                        disLikeR.setVisible(false);
                    }

                    Rectangle smileR = new Rectangle(25, 25);
                    smileR.setFill(new ImagePattern(new Image("file:assets/smile.png", false)));
                    Label smileCount = new Label();
                    smileCount.setStyle("-fx-font-size: 15;" + "-fx-text-fill: white;");
                    smileCount.setPrefSize(25, 25);
                    int smileCountInt = message.getReactCount("smile");
                    if (smileCountInt > 0) {
                        smileR.setVisible(true);
                        smileCount.setText(String.valueOf(smileCountInt));
                    } else {
                        smileR.setVisible(false);
                    }

                    hBox.getChildren().addAll(messageContent, likeR, likeCount, disLikeR, disLikeCount, smileR, smileCount);
                    setGraphic(hBox);

                    setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.isSecondaryButtonDown()) {
                                VBox vBox = new VBox();
                                vBox.setAlignment(Pos.CENTER);
                                vBox.setPrefSize(50, 150);
                                vBox.setStyle("-fx-background-color: #202225");


                                ImageView like = new ImageView();
                                like.setImage(new Image("file:assets/like.png", false));


                                ImageView disLike = new ImageView();
                                disLike.setImage(new Image("file:assets/dislike.png", false));
                                disLike.setLayoutY(like.getLayoutY() + 5);

                                ImageView smile = new ImageView();
                                smile.setImage(new Image("file:assets/smile.png", false));

                                ImageView pin = new ImageView();
                                pin.setImage(new Image("file:assets/pin.png", false));

                                Stage popupStage = new Stage(StageStyle.TRANSPARENT);
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                popupStage.initOwner(stage);
                                popupStage.initModality(Modality.APPLICATION_MODAL);
                                popupStage.setY(event.getScreenY() + 10);
                                popupStage.setX(event.getScreenX() + 10);
                                vBox.getChildren().addAll(like, disLike, smile, pin);
                                Scene scene = new Scene(vBox);
                                popupStage.setScene(scene);
                                popupStage.show();
                                like.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        int index = messageList.getItems().indexOf(messageList.getSelectionModel().getSelectedItem());
                                        Message selected = messageList.getSelectionModel().getSelectedItem();
                                        selected.setReaction("like", DiscordApplication.user.getUsername());
                                        Message likeCommand = new Message("#react>" + index + ">" + "like", DiscordApplication.user.getUsername(), LocalDateTime.now());
                                        DiscordApplication.appController.sendMessageToDirectChat(DiscordApplication.user.getUsername(), friendInChat.getUsername(), likeCommand);
                                        likeR.setVisible(true);
                                        likeCount.setText(String.valueOf(selected.getReactCount("like")));
                                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                                        stage.close();
                                    }
                                });


                                disLike.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        int index = messageList.getItems().indexOf(messageList.getSelectionModel().getSelectedItem());
                                        Message selected = messageList.getSelectionModel().getSelectedItem();
                                        selected.setReaction("dislike", DiscordApplication.user.getUsername());
                                        Message disLikeCommand = new Message("#react>" + index + ">" + "dislike", DiscordApplication.user.getUsername(), LocalDateTime.now());
                                        DiscordApplication.appController.sendMessageToDirectChat(DiscordApplication.user.getUsername(), friendInChat.getUsername(), disLikeCommand);
                                        messageList.getItems().get(index).setReaction("dislike", DiscordApplication.user.getUsername());
                                        disLikeR.setVisible(true);
                                        disLikeCount.setText(String.valueOf(selected.getReactCount("dislike")));
                                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                        stage.close();
                                    }
                                });

                                smile.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        int index = messageList.getItems().indexOf(messageList.getSelectionModel().getSelectedItem());
                                        Message selected = messageList.getSelectionModel().getSelectedItem();
                                        selected.setReaction("smile", DiscordApplication.user.getUsername());
                                        Message smileCommand = new Message("#react>" + index + ">" + "smile", DiscordApplication.user.getUsername(), LocalDateTime.now());
                                        DiscordApplication.appController.sendMessageToDirectChat(DiscordApplication.user.getUsername(), friendInChat.getUsername(), smileCommand);
                                        messageList.getItems().get(index).setReaction("smile", DiscordApplication.user.getUsername());
                                        smileR.setVisible(true);

                                        smileCount.setText(String.valueOf(selected.getReactCount("smile")));
                                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                        stage.close();

                                    }
                                });

                                pin.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        int index = messageList.getItems().indexOf(messageList.getSelectionModel().getSelectedItem());
                                        Message messageToPin = messageList.getSelectionModel().getSelectedItem();
                                        Message pinCommand = new Message("#pin>" + index, DiscordApplication.user.getUsername(), LocalDateTime.now());
                                        DiscordApplication.appController.sendMessageToDirectChat(DiscordApplication.user.getUsername(), friendInChat.getUsername(), pinCommand);
                                        addToPins(messageToPin);
                                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                        stage.close();

                                    }
                                });


                                //  System.out.println("clicked on " + messageList.getSelectionModel().getSelectedItem());

                            }
                            event.consume();
                        }
                    });

                } else {
                    setStyle("-fx-background-color: #36393f;" + "-fx-padding: 10px;" + "-fx-text-fill: #36393f;");
                }

            }

        });

        // messageList.setStyle("-fx-background-color: #36393f;" + "-fx-padding: 10px;");
    }

    /**
     * Send message.
     *
     * @param event the event
     */
    @FXML
    void sendMessage(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String content = messageTF.getText();
            if (content != null && !content.equals("")) {
                String author = DiscordApplication.user.getUsername();
                Message message = new Message(content, author, LocalDateTime.now());
                messageTF.clear();
                DiscordApplication.appController.sendMessageToDirectChat(author, friendInChat.getUsername(), message);
            }
        }

    }

    /**
     * Send file message.
     *
     * @param event the event
     */
    @FXML
    void sendFileMessage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String content = "#file>" + file.getAbsolutePath();
            String author = DiscordApplication.user.getUsername();
            Message fileMessage = new Message(content, author, LocalDateTime.now(), true);
            DiscordApplication.appController.sendMessageToDirectChat(author, friendInChat.getUsername(), fileMessage);
        }
    }

    /**
     * Save file in downloads string.
     *
     * @param message the message
     * @return the string
     */
    public String saveFileInDownloads(Message message) {
        try {
            byte[] bytes = message.getFile();
            String name = message.getFileName();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(name);
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                OutputStream os = new FileOutputStream(file);
                os.write(bytes);
                os.close();
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "couldn't save file.";
        }

    }

    private boolean exitFromDirectChat() {
        if (listenToMsg != null && listenToMsg.isAlive()) {
            DiscordApplication.appController.sendMessageToDirectChat(
                    DiscordApplication.user.getUsername(), friendInChat.getUsername(),
                    new Message("#exit", DiscordApplication.user.getUsername(), LocalDateTime.now()));
            TabPane friendPane = (TabPane) friendTabPane.getChildren().get(0);
            friendPane.setVisible(true);
            friendPane.setDisable(false);
            AnchorPane directChat = (AnchorPane) friendTabPane.getChildren().get(1);
            Circle circle = (Circle) directChat.getChildren().get(2);
            circle.setFill(new ImagePattern(new Image("file:assets/plus.png")));
            directChat.setVisible(false);
            directChat.setDisable(true);
            return true;
        }
        return false;
    }

    /**
     * Show pinned list.
     */
    void showPinnedList() {
        ArrayList<Message> pins = DiscordApplication.appController.getPinnedMessages(DiscordApplication.user.getUsername(), friendInChat.getUsername());
        ObservableList<Message> ObsPins = FXCollections.observableArrayList(pins);
        pinBox.setItems(ObsPins);
        pinBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Message message, boolean b) {
                super.updateItem(message, b);
                if (message != null & !b) {

                    if (!message.isFile()) {
                        setText(message.toString());
                        setStyle("-fx-background-color: #36393f;" +
                                "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold");
                    } else {
                        setCursor(Cursor.HAND);
                        setText(message.fileToString());

                        setStyle("-fx-background-color: #36393f;" +
                                "-fx-text-fill: rgba(7,141,242,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold");
                        setOnMouseReleased(e -> {
                            saveFileInDownloads(message);
                        });
                    }

                } else {
                    setStyle("-fx-background-color: #36393f;" +
                            "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold");
                }

            }
        });
    }

    /**
     * Add to pins.
     *
     * @param messageToPin the message to pin
     */
    void addToPins(Message messageToPin) {
        Platform.runLater(() -> pinBox.getItems().add(messageToPin));
    }


}

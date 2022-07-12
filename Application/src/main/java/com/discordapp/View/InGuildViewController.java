package com.discordapp.View;

import com.discordapp.Model.Guild;
import com.discordapp.Model.GuildUser;
import com.discordapp.Model.Message;
import com.discordapp.Model.TextChannel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
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
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.nio.channels.Channel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The type In guild view controller.
 */
public class InGuildViewController {

    /**
     * The constant currGuild.
     */
    public static Guild currGuild;

    @FXML
    private Circle settingIcon;
    @FXML
    private Circle status;
    @FXML
    private ListView<GuildUser> userList;
    @FXML
    private Circle addServerIcon;
    @FXML
    private Circle avatar;
    @FXML
    private Circle discordIcon;
    @FXML
    private ListView<TextChannel> channelList;
    @FXML
    private ListView<Guild> guildList;
    @FXML
    private Circle paperclip;

    @FXML
    private Circle pinIcon;


    @FXML
    private ListView<Message> serverMsgList;
    @FXML
    private ComboBox<Message> pinBox;
    @FXML
    private TextField messageTF;
    /**
     * The Listen to msg.
     */
    Thread listenToMsg;
    /**
     * The Curr text channel.
     */
    TextChannel currTextChannel;

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        setAvatar();
        setDiscordIcon();
        setAddServerIcon();
        setSettingIcon();
        showGuilds();
        setStatus();
        setSendIcon();
        setPinIcon();
        showGuildUsers();
        showTextChannel();
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
     * Sets status.
     */
    public void setStatus() {
        status.setFill(StatusViewController.color);
    }

    private void setSendIcon() {
        paperclip.setFill(new ImagePattern(new Image("file:assets/plus.png", false)));
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
                        exitFromGroupChat();
                        InGuildViewController.currGuild = guildList.getSelectionModel().getSelectedItem();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-guild-view.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        DiscordApplication.loadNewScene(loader, stage);

                    });

                }
                setStyle("-fx-background-color: #202225;" + "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-padding: 15px;");
            }
        });

    }

    private void showGuildUsers() {
        ArrayList<GuildUser> guildUsers = new ArrayList<>(currGuild.getGuildUsers());
        ObservableList<GuildUser> guildUsersObL = FXCollections.observableList(guildUsers);
        userList.setItems(guildUsersObL);
        userList.setCellFactory(param -> new ListCell<>() {

            @Override
            public void updateItem(GuildUser guildUser, boolean empty) {
                super.updateItem(guildUser, empty);
                setText(null);
                setGraphic(null);
                if (guildUser != null && !empty) {
                    setAlignment(Pos.CENTER);
                    setTextAlignment(TextAlignment.CENTER);
                    if (guildUser.getUsername().equals(currGuild.getOwnerName())) {
                        setText("<Owner>\n" + guildUser.getUsername());
                    } else {
                        setText(guildUser.getUsername());
                    }

                    setStyle("-fx-background-color: #2f3136;" + "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 20;" + "-fx-font-weight: bold;" + "-fx-padding: 10px");
                } else {
                    setStyle("-fx-background-color:  #2f3136;" + "-fx-padding: 10px");
                }
            }
        });
    }


    private void showTextChannel() {
        ArrayList<TextChannel> txtChannel = currGuild.getTextChannels();
        ObservableList<TextChannel> txtChannelObL = FXCollections.observableList(txtChannel);
        channelList.setItems(txtChannelObL);
        channelList.setCellFactory(param -> new ListCell<>() {
            @Override
            public void updateItem(TextChannel textChannel, boolean empty) {
                super.updateItem(textChannel, empty);

                setText(null);
                setGraphic(null);
                if (textChannel != null && !empty) {
                    setOnMouseReleased(e -> {
                        currTextChannel = getItem();
                        goToGroupChat(getItem());

                    });
                    setAlignment(Pos.CENTER);
                    setTextAlignment(TextAlignment.CENTER);
                    setText(textChannel.getName());
                    setStyle("-fx-background-color: #2f3136;" + "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 20;" + "-fx-font-weight: bold;");
                } else {
                    setStyle("-fx-background-color:  #2f3136;");
                }
            }
        });
    }

    private void goToGroupChat(TextChannel channel) {
        if (!exitFromGroupChat()) {
            loadGroupChatMessages();
            showPinnedList();
            listenToMsg = new Thread(() -> {
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
            });
            listenToMsg.start();
        }
    }
    private void loadGroupChatMessages() {
        ArrayList<Message> msgs = DiscordApplication.appController.getTextChannelMessages(currGuild, currTextChannel);
        ObservableList<Message> obsMessage = FXCollections.observableArrayList(msgs);
        serverMsgList.setItems(obsMessage);
        serverMsgList.setCellFactory(e -> new ListCell<>() {
            @Override
            protected void updateItem(Message message, boolean b) {
                setText(null);
                setGraphic(null);
                if (message != null && !b) {
                    super.updateItem(message, b);
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
                                        int index = serverMsgList.getItems().indexOf(serverMsgList.getSelectionModel().getSelectedItem());
                                        Message selected = serverMsgList.getSelectionModel().getSelectedItem();
                                        selected.setReaction("like", DiscordApplication.user.getUsername());
                                        Message likeCommand = new Message("#react>" + index + ">" + "like", DiscordApplication.user.getUsername(), LocalDateTime.now());
                                        DiscordApplication.appController.sendMessageToGroupChat(currGuild.getOwnerName(), currGuild.getName(), currTextChannel.getName(), likeCommand); //TODO: send msg To group chat
                                        likeR.setVisible(true);
                                        likeCount.setText(String.valueOf(selected.getReactCount("like")));
                                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                                        stage.close();
                                    }
                                });


                                disLike.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        int index = serverMsgList.getItems().indexOf(serverMsgList.getSelectionModel().getSelectedItem());
                                        Message selected = serverMsgList.getSelectionModel().getSelectedItem();
                                        selected.setReaction("dislike", DiscordApplication.user.getUsername());
                                        Message disLikeCommand = new Message("#react>" + index + ">" + "dislike", DiscordApplication.user.getUsername(), LocalDateTime.now());
                                        DiscordApplication.appController.sendMessageToGroupChat(currGuild.getOwnerName(), currGuild.getName(), currTextChannel.getName(), disLikeCommand); //TODO: send msg To group chat
                                        serverMsgList.getItems().get(index).setReaction("dislike", DiscordApplication.user.getUsername());
                                        disLikeR.setVisible(true);
                                        disLikeCount.setText(String.valueOf(selected.getReactCount("dislike")));
                                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                        stage.close();
                                    }
                                });

                                smile.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        int index = serverMsgList.getItems().indexOf(serverMsgList.getSelectionModel().getSelectedItem());
                                        Message selected = serverMsgList.getSelectionModel().getSelectedItem();
                                        selected.setReaction("smile", DiscordApplication.user.getUsername());
                                        Message smileCommand = new Message("#react>" + index + ">" + "smile", DiscordApplication.user.getUsername(), LocalDateTime.now());
                                        DiscordApplication.appController.sendMessageToGroupChat(currGuild.getOwnerName(), currGuild.getName(), currTextChannel.getName(), smileCommand); //TODO: send msg To group chat
                                        serverMsgList.getItems().get(index).setReaction("smile", DiscordApplication.user.getUsername());
                                        smileR.setVisible(true);

                                        smileCount.setText(String.valueOf(selected.getReactCount("smile")));
                                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                        stage.close();

                                    }
                                });

                                pin.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        int index = serverMsgList.getItems().indexOf(serverMsgList.getSelectionModel().getSelectedItem());
                                        Message messageToPin = serverMsgList.getSelectionModel().getSelectedItem();
                                        Message pinCommand = new Message("#pin>" + index, DiscordApplication.user.getUsername(), LocalDateTime.now());
                                        DiscordApplication.appController.sendMessageToGroupChat(currGuild.getOwnerName(), currGuild.getName(), currTextChannel.getName(), pinCommand); //TODO: send msg To group chat
                                        addToPins(messageToPin);
                                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                        stage.close();

                                    }
                                });


                                //  System.out.println("clicked on " + serverMsgList.getSelectionModel().getSelectedItem());

                            }
                            event.consume();
                        }
                    });

                } else {
                    setStyle("-fx-background-color: #36393f;" + "-fx-padding: 10px;" + "-fx-text-fill: #36393f;");
                }

            }

        });
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
                DiscordApplication.appController.sendMessageToGroupChat(currGuild.getOwnerName(), currGuild.getName(), currTextChannel.getName(), message); //TODO : send message to GroupChat
            }
        }

    }

    @FXML
    private void serverSetting(MouseEvent event) throws IOException {
        exitFromGroupChat();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getScene().getRoot();
        ColorAdjust adj = new ColorAdjust(0, 0.1, -0.2, 0);
        GaussianBlur blur = new GaussianBlur(5);
        adj.setInput(blur);
        root.setEffect(adj);

        Stage popupStage = new Stage(StageStyle.TRANSPARENT);
        popupStage.initOwner(stage);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("server-setting-view.fxml"));
        popupStage.setScene(new Scene(loader.load(), Color.TRANSPARENT));
        popupStage.show();

    }


    @FXML
    private void goToinApp(MouseEvent event) {
        if(!exitFromGroupChat()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            DiscordApplication.loadNewScene(loader, stage);
        }
    }

    /**
     * Go to acc settings.
     *
     * @param event the event
     */
    @FXML
    void goToAccSettings(MouseEvent event) {
        exitFromGroupChat();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }

    private boolean exitFromGroupChat() {
        if (listenToMsg != null && listenToMsg.isAlive()) {
            DiscordApplication.appController.sendMessageToGroupChat(currGuild.getOwnerName(),currGuild.getName(),currTextChannel.getName(),
                    new Message("#exit", DiscordApplication.user.getUsername(), LocalDateTime.now()));

            return true;
        }
        return false;
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

    /**
     * Add message.
     *
     * @param incomingMessage the incoming message
     */
    public void addMessage(Message incomingMessage) {
        Platform.runLater(() -> {
            serverMsgList.getItems().add(incomingMessage);
        });
    }

    /**
     * Show pinned list.
     */
    void showPinnedList() {
        ArrayList<Message> pins = DiscordApplication.appController.getGroupChatPinnedMessages(currGuild.getOwnerName(), currGuild.getName(), currTextChannel.getName());
        ObservableList<Message> ObsPins = FXCollections.observableArrayList(pins);
        pinBox.setItems(ObsPins);
        pinBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Message message, boolean b) {
                super.updateItem(message, b);
                if (message != null && !b) {

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
            DiscordApplication.appController.sendMessageToGroupChat(currGuild.getOwnerName(), currGuild.getName(), currTextChannel.getName(), fileMessage);
        }
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

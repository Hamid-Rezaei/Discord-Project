package com.discordapp.View;

import com.discordapp.Model.Guild;
import com.discordapp.Model.GuildUser;
import com.discordapp.Model.Message;
import com.discordapp.Model.TextChannel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashSet;

public class InGuildViewController {

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
    private ListView<Message> serverMsgList;


    @FXML
    public void initialize() {
        setAvatar();
        setDiscordIcon();
        setAddServerIcon();
        setSettingIcon();
        showGuilds();
        setStatus();
        showGuildUsers();
        showTextChannel();
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

    public void setStatus() {
        status.setFill(StatusViewController.color);
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

                    setStyle("-fx-background-color: #2f3136;" + "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 20;" + "-fx-font-weight: bold;");
                } else {
                    setStyle("-fx-background-color:  #2f3136;");
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


    @FXML
    void serverSetting(MouseEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getScene().getRoot();
        ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
        GaussianBlur blur = new GaussianBlur(5);
        adj.setInput(blur);
        root.setEffect(adj);

        Stage popupStage = new Stage(StageStyle.UTILITY);
        popupStage.initOwner(stage);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        //popupStage.setY(event.getScreenY() + 25);
        //popupStage.setX(event.getScreenX() + 5);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("server-setting-view.fxml"));
        popupStage.setScene(new Scene(loader.load(), Color.TRANSPARENT));
        popupStage.show();

    }


    @FXML
    void goToinApp(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }

}

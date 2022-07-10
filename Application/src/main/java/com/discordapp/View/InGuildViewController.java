package com.discordapp.View;

import com.discordapp.Model.Guild;
import com.discordapp.Model.GuildUser;
import com.discordapp.Model.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.nio.channels.Channel;

public class InGuildViewController {

    public static Guild guild;

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
    private ListView<Channel> channelList;
    @FXML
    private ListView<Guild> guildList;
    @FXML
    private Circle paperclip;
    @FXML
    private ListView<Message> serverMsgList;


    @FXML
    public void initialize(){
        setAvatar();
        setDiscordIcon();
        setAddServerIcon();
        setSettingIcon();
        showGuilds();
        setStatus();
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
                    setOnMouseClicked(event ->{
                        InGuildViewController.guild = guildList.getSelectionModel().getSelectedItem();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-guild-view.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        DiscordApplication.loadNewScene(loader, stage);

                    });

                }
                setStyle("-fx-background-color: #202225;" + "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-padding: 15px;");
            }
        });

    }

    @FXML
    void changeStatus(MouseEvent event) {

    }

    @FXML
    void serverSetting(ActionEvent event) {

    }



    @FXML
    void goToinApp(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DiscordApplication.loadNewScene(loader, stage);
    }

}

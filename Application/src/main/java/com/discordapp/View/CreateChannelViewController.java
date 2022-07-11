package com.discordapp.View;

import com.discordapp.Model.Guild;
import com.discordapp.Model.Message;
import com.discordapp.Model.TextChannel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.nio.channels.Channel;

public class CreateChannelViewController {

    private ListView<TextChannel> listTxtChannel;
    @FXML
    private TextField channelNameTF;


    @FXML
    public void initialize(ListView<TextChannel> listTxtChannel){
        this.listTxtChannel = listTxtChannel;
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = stage.getOwner().getScene().getRoot();
        root.setEffect(null);
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-guild-view.fxml"));
        DiscordApplication.loadNewScene(loader, (Stage) stage.getOwner());
    }

    @FXML
    void createChannel(ActionEvent event) {
        Guild currGuild = InGuildViewController.currGuild;
        String respone = DiscordApplication.appController.addNewTextChannel(currGuild, channelNameTF.getText());
        System.out.println(respone);
        InGuildViewController.currGuild = DiscordApplication.appController.getGuild(currGuild.getOwnerName(), currGuild.getName());
        cancel(event);
    }
    
}

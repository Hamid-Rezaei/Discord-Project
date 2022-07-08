package com.discordapp.View;

import com.discordapp.Model.Guild;
import com.discordapp.Model.GuildUser;
import com.discordapp.Model.Role;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddServerController {
    ListView<Guild> guildList;
    @FXML
    private TextField serverNameTF;

    @FXML
    public void initialize(ListView<Guild> guilds){
        guildList = guilds;
    }

    @FXML
    private void closeAddServerPop(ActionEvent event) {
        closePopupStage(event);
    }

    @FXML
    public void createServer(ActionEvent event) {
        String serverName = serverNameTF.getText();
        GuildUser owner = new GuildUser(DiscordApplication.user, new Role("owner"));
        Guild guild = new Guild(serverName, owner);
        DiscordApplication.appController.addServer(guild);
        closePopupStage(event);
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
                }
                setStyle("-fx-background-color: #202225;" + "-fx-text-fill: rgba(234,238,238,0.89) ;" + "-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-padding: 15px;");
            }
        });

    }
    private void closePopupStage(ActionEvent event) {
        showGuilds();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("in-application-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}

//package com.discordapp.model.guild;
//
//import com.discordapp.model.Message;
//
package com.discordapp.Model;

import com.discordapp.controller.Connection;
import com.discordapp.Model.Message;

import java.io.*;
import java.util.HashSet;

/**
 * The type Group chat.
 */
public class GroupChat implements Serializable {
    /**
     * The Users in chat.
     */
    transient HashSet<Connection> usersInChat;

    /**
     * Instantiates a new Group chat.
     */
    public GroupChat() {
        usersInChat = new HashSet<>();
    }

    /**
     * Instantiates a new Group chat.
     *
     * @param usersInChat the users in chat
     */
    public GroupChat(HashSet<Connection> usersInChat) {
        this.usersInChat = usersInChat;
    }

    /**
     * Add message.
     *
     * @param message the message
     */
    public void addMessage(Message message){
        sendMessage(message);
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(Message message){
        for(Connection user: usersInChat){
            user.sendMessage(message.toString());
        }
    }

    /**
     * Broadcast message.
     *
     * @param message the message
     */
    public void broadcastMessage(Message message) {
        for (Connection connection : usersInChat) {
            connection.sendMessage(message.toString());
        }
    }


    /**
     * Add user.
     *
     * @param user the user
     */
    public void addUser(Connection user) {
        usersInChat.add(user);

    }

    /**
     * Remove user.
     *
     * @param user the user
     */
    public void removeUser(Connection user) {
        usersInChat.remove(user);
    }


}


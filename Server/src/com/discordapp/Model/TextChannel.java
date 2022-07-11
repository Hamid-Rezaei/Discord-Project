package com.discordapp.Model;

import com.discordapp.Controller.Connection;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The type Text channel.
 */
public class TextChannel extends Channel {
    /**
     * The Messages.
     */
    ArrayList<Message> messages;
    /**
     * The Users in chat.
     */
    transient HashSet<Connection> usersInChat;
    /**
     * The Guild name.
     */
    String guildName;
    /**
     * The Pinned messages.
     */
    ArrayList<Message> pinnedMessages;

    /**
     * Instantiates a new Text channel.
     *
     * @param name      the name
     * @param groupChat the group chat
     * @param guildName the guild name
     */
    public TextChannel(String name, GroupChat groupChat, String guildName) {
        super(name, groupChat);
        messages = new ArrayList<>();
        this.guildName = guildName;
        usersInChat = new HashSet<>();
        pinnedMessages = new ArrayList<>();
    }

    /**
     * Instantiates a new Text channel.
     *
     * @param name      the name
     * @param guildName the guild name
     */
    public TextChannel(String name, String guildName) {
        super(name);
        messages = new ArrayList<>();
        this.guildName = guildName;
        usersInChat = new HashSet<>();
        pinnedMessages = new ArrayList<>();
    }


    /**
     * Add message.
     *
     * @param message the message
     */
    public synchronized void addMessage(Message message) {
        this.messages.add(message);
        saveMessages();
        broadcastMessage(message);
    }

    /**
     * Gets message index.
     *
     * @param message the message
     * @return the message index
     */
    public int getMessageIndex(Message message) {
        return messages.indexOf(message);
    }

    /**
     * Broadcast message.
     *
     * @param message the message
     */
    public void broadcastMessage(Message message) {
        for (Connection connection : usersInChat) {
            connection.sendMessage(message);
        }
    }

    /**
     * Broadcast messages.
     *
     * @param connection the connection
     */
    public synchronized void broadcastMessages(Connection connection) {
        if (this.messages.size() > 15) {
            for (int i = messages.size() - 15; i < messages.size(); i++) {
                connection.sendMessage(messages.get(i));
            }
        } else {
            for (int i = 0; i < messages.size(); i++) {
                connection.sendMessage(messages.get(i));

            }
        }

    }

    /**
     * Broadcast exit message.
     *
     * @param message        the message
     * @param userConnection the user connection
     */
    public void broadcastExitMessage(String message, Connection userConnection) {
        for (Connection connection : usersInChat) {
            if (connection.getUsername().equals(userConnection.getUsername())) {
                connection.sendMessage(message);
            }
        }
    }

    /**
     * Pin message.
     *
     * @param index the index
     */
    public synchronized void pinMessage(int index) {
        pinnedMessages.add(messages.get(index - 1));
        saveMessages();
    }

    /**
     * Show pinned messages.
     *
     * @param connection the connection
     */
    public synchronized void showPinnedMessages(Connection connection) {
        for (int i = 0; i < pinnedMessages.size(); i++) {
            connection.sendMessage(pinnedMessages.get(i));
        }
    }

    /**
     * Add user.
     *
     * @param user the user
     */
    public void addUser(Connection user) {
        if (usersInChat == null) {
            usersInChat = new HashSet<>();
        }
        usersInChat.add(user);
        broadcastMessages(user);
    }

    /**
     * Remove user.
     *
     * @param user the user
     */
    public void removeUser(Connection user) {
        usersInChat.remove(user);
    }

    /**
     * Save messages.
     */
    public synchronized void saveMessages() {
        File theDir = new File("assets/guilds/" + guildName + "/textchannels/");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        try (FileOutputStream writeData = new FileOutputStream("assets/guilds/" + guildName + "/textchannels/" + this.name + ".bin");
             ObjectOutputStream writeStream = new ObjectOutputStream(writeData)) {
            writeStream.writeObject(messages);
            writeStream.flush();
            writeStream.close();
            writeData.close();
            FileOutputStream pinnedData = new FileOutputStream("assets/guilds/" + guildName + "/textchannels/" + this.name + "-" + "pin.bin");
            ObjectOutputStream write = new ObjectOutputStream(pinnedData);
            write.writeObject(pinnedMessages);
            write.flush();
            write.close();
            pinnedData.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load messages.
     */
    public void loadMessages() {
        try {
            FileInputStream readData = new FileInputStream("assets/guilds/" + guildName + "/textchannels/" + this.name + ".bin");
            ObjectInputStream readStream = new ObjectInputStream(readData);
            messages = (ArrayList<Message>) readStream.readObject();
            FileInputStream pinData = new FileInputStream("assets/guilds/" + guildName + "/textchannels/" + this.name + "-" + "pin.bin");
            ObjectInputStream readPinned = new ObjectInputStream(pinData);
            pinnedMessages = (ArrayList<Message>) readPinned.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Here we have some bugs");
            saveMessages();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * React to message.
     *
     * @param index        the index
     * @param reactionType the reaction type
     * @param reactor      the reactor
     */
    public void reactToMessage(int index, String reactionType, String reactor) {
        messages.get(index).setReaction(reactionType,reactor);
        saveMessages();
    }
}

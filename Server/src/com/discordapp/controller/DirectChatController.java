package com.discordapp.controller;

import com.discordapp.Model.Chat;
import com.discordapp.Model.Message;
import com.discordapp.Model.User;

import java.io.*;
import java.util.*;

/**
 * The type Direct chat com.discordapp.controller.
 */
public class DirectChatController implements Runnable {

    private Chat directChat;
    private ArrayList<Message> messages = new ArrayList<>();
    private String chatHashCode;
    private ArrayList<User> participants;
    private HashSet<Connection> usersInChatConnection;
    private ArrayList<Message> pinnedMessages = new ArrayList<>();

    /**
     * Instantiates a new Direct chat com.discordapp.controller.
     *
     * @param usersInChatConnection the users in chat connection
     * @param participants          the participants
     */
    public DirectChatController(HashSet<Connection> usersInChatConnection, ArrayList<User> participants) {
        directChat = new Chat();
        this.usersInChatConnection = usersInChatConnection;
        this.participants = participants;
        this.chatHashCode = generateHashCode();
        directChat = new Chat(messages);
    }


    /**
     * Generate hash code string.
     *
     * @return the string
     */
    public String generateHashCode() {
        String hash;
        String user_1 = participants.get(0).getUsername();
        String user_2 = participants.get(1).getUsername();
        if (user_1.length() < user_2.length()) {
            hash = user_1 + user_2;
        } else {
            hash = user_2 + user_1;
        }
        return hash;
    }

    /**
     * Add message.
     *
     * @param message the message
     */
    public synchronized void addMessage(Message message) {
        messages.add(message);
        saveMessages();
        //broadcastMessage(message); not usable in gui
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    /**
     * Num of users in chat int.
     *
     * @return the int
     */
    public int numOfUsersInChat() {
        return usersInChatConnection.size();
    }

    /**
     * Get message index int.
     *
     * @param message the message
     * @return the int
     */
    public int getMessageIndex(Message message){
        for(int i = 0; i< messages.size();i++){
            if(messages.get(i).equals(message)){
                return i + 1;
            }
        }
        return 0;
    }

    /**
     * Broadcast message.
     *
     * @param message the message
     */
    public void broadcastMessage(Message message) {

        int index = messages.indexOf(message);
        if(message.isFile())
            index = -2;
        for (Connection connection : usersInChatConnection) {
            connection.sendMessage(message,index + 1);
        }
    }

    /**
     * Broadcast exit message.
     *
     * @param message        the message
     * @param userConnection the user connection
     */
    public void broadcastExitMessage(String message, Connection userConnection) {
        for (Connection connection : usersInChatConnection) {
            if (connection.getUsername().equals(userConnection.getUsername())) {
                connection.sendMessage(message);
            }
        }
    }

    /**
     * Broadcast messages.
     *
     * @param connection the connection
     */
    public synchronized void broadcastMessages(Connection connection) {
        if (this.messages.size() > 6) {
            for (int i = messages.size() - 6; i < messages.size(); i++) {
                connection.sendMessage((i + 1) + ". " + messages.get(i).toString());
            }
        } else {
            for (int i = 0; i < messages.size(); i++) {
                connection.sendMessage((i + 1) + ". " + messages.get(i).toString());

            }
        }

    }

    /**
     * Pin message.
     *
     * @param index the index
     */
    public synchronized void pinMessage(int index) {
        pinnedMessages.add(messages.get(index));
        saveMessages();
    }

    /**
     * returns arrayList of pinned messages
     * @return array list of pinned messages
     */
    public ArrayList<Message> getPinnedMessages() {
        return pinnedMessages;
    }

    /**
     * Show pinned messages.
     *
     * @param connection the connection
     */
    public synchronized void showPinnedMessages(Connection connection) {
        for (int i = 0; i < pinnedMessages.size(); i++) {
            connection.sendMessage(pinnedMessages.get(i), (i + 1));
        }
    }

    /**
     * Add connection.
     *
     * @param connection the connection
     */
    public synchronized void addConnection(Connection connection) {
        usersInChatConnection.add(connection);
    }

    /**
     * Remove connection.
     *
     * @param username the username
     */
    public synchronized void removeConnection(String username) {
        Iterator<Connection> it = usersInChatConnection.iterator();
        while (it.hasNext()) {
            Connection connection = it.next();
            if (connection.getUsername().equals(username)) {
                it.remove();
            }
        }

    }

    /**
     * Save messages.
     */
    public synchronized void saveMessages() {
        File theDir = new File("assets/direct_chat");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        try (FileOutputStream writeData = new FileOutputStream("assets/direct_chat/" + chatHashCode + ".bin");
             ObjectOutputStream writeStream = new ObjectOutputStream(writeData)) {
            writeStream.writeObject(messages);
            writeStream.flush();
            writeStream.close();
            writeData.close();
            FileOutputStream pinnedData = new FileOutputStream("assets/direct_chat/" + chatHashCode + "-" + "pin.bin");
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
            FileInputStream readData = new FileInputStream("assets/direct_chat/" + chatHashCode + ".bin");
            ObjectInputStream readStream = new ObjectInputStream(readData);
            messages = (ArrayList<Message>) readStream.readObject();
            FileInputStream pinData = new FileInputStream("assets/direct_chat/" + chatHashCode + "-" + "pin.bin");
            ObjectInputStream readPinned = new ObjectInputStream(pinData);
            pinnedMessages = (ArrayList<Message>) readPinned.readObject();
        } catch (FileNotFoundException e) {
            //System.out.println("Here we have some bugs");
            saveMessages();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets direct chat.
     *
     * @return the direct chat
     */
    public Chat getDirectChat() {
        return directChat;
    }

    /**
     * Gets chat hash code.
     *
     * @return the chat hash code
     */
    public String getChatHashCode() {
        return chatHashCode;
    }

    @Override
    public void run() {
        while (true) {

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectChatController)) return false;
        DirectChatController that = (DirectChatController) o;
        return Objects.equals(getDirectChat(), that.getDirectChat()) && Objects.equals(messages, that.messages) && Objects.equals(getChatHashCode(), that.getChatHashCode()) && Objects.equals(participants, that.participants) && Objects.equals(usersInChatConnection, that.usersInChatConnection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDirectChat(), messages, getChatHashCode(), participants, usersInChatConnection);
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

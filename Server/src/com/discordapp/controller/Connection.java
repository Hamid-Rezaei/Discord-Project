package com.discordapp.controller;

import com.discordapp.Model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The type Connection.
 */
public class Connection {
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket socket;
    private String username;

    /**
     * Instantiates a new Connection.
     *
     * @param socket       the socket
     * @param outputStream the output stream
     * @param inputStream  the input stream
     * @param username     the username
     * @throws IOException the io exception
     */
    public Connection(Socket socket,ObjectOutputStream outputStream,ObjectInputStream inputStream, String username) throws IOException {
        this.socket = socket;
        this.username = username;
        this.outputStream = outputStream; // new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = inputStream; //new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Send message.
     *
     * @param message the message
     * @param index   the index
     */
    public void sendMessage(Message message, int index) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
            if(index != -1) {
                Integer ind = index;
                outputStream.writeObject(ind);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Receive message message.
     *
     * @return the message
     */
    public Message receiveMessage() {
        try {
            return (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Send all messages.
     *
     * @param messages the messages
     */
    public void sendAllMessages(ArrayList<Message> messages) {
        try {
            outputStream.writeObject(messages);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive all messages array list.
     *
     * @return the array list
     */
    public ArrayList<Message> receiveAllMessages() {
        try {
            return (ArrayList<Message>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connection)) return false;
        Connection that = (Connection) o;
        return Objects.equals(outputStream, that.outputStream) && Objects.equals(inputStream, that.inputStream) && Objects.equals(socket, that.socket) && Objects.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputStream, inputStream, socket, getUsername());
    }
}

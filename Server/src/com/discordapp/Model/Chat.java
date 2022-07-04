package com.discordapp.Model;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * The type Chat.
 */
public class Chat implements Runnable, Serializable {
    private ArrayList<Message> messages;
    private User currUser;
    private transient ObjectOutputStream outputStream;
    private transient ObjectInputStream inputStream;
    private volatile boolean exit = false;
    /**
     * The Input.
     */
    String input = "";

    /**
     * Instantiates a new Chat.
     */
    public Chat() {
        messages = new ArrayList<>();
    }

    /**
     * Sets curr user.
     *
     * @param currUser the curr user
     */
    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    /**
     * Sets output stream.
     *
     * @param outputStream the output stream
     */
    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Sets input stream.
     *
     * @param inputStream the input stream
     */
    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Instantiates a new Chat.
     *
     * @param messages the messages
     */
    public Chat(ArrayList<Message> messages) {
        this.messages = messages;
    }

    /**
     * Gets messages.
     *
     * @return the messages
     */
    public ArrayList<Message> getMessages() {
        return messages;
    }

    /**
     * Add message.
     *
     * @param message the message
     */
    public void addMessage(Message message) {
        messages.add(message);
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
            String path = "downloads/" + currUser.getUsername() + "/from_" + message.getAuthorName();
            File theDir = new File(path);
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            File file = new File(path + "/" + name);
            OutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return "couldn't save file.";
        }

    }

    /**
     * has a thread for sending messages and a while loop for listening to incoming messages.
     */
    @Override
    public void run() {
        exit = false;
        Scanner scanner = new Scanner(System.in);
        Thread snedMsg = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!input.equals("#exit")) {
                    input = scanner.nextLine();
                    Message message;
                    if (input.startsWith("#file")) {
                        message = new Message(input, currUser.getUsername(), LocalDateTime.now(), true);
                    } else {
                        message = new Message(input, currUser.getUsername(), LocalDateTime.now());
                    }
                    try {
                        outputStream.writeObject(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    input = scanner.nextLine();
                    messages.add(message);
                }
            }
        });
        snedMsg.start();

        while (!input.equals("#exit")) {
            try {
                Object message = inputStream.readObject();
                if (message instanceof Message msg) {
                    if (msg.isFile()) {
                        if (!msg.getAuthorName().equals(currUser.getUsername()))
                            System.out.println("file saved in: " + saveFileInDownloads(msg));
                    } else {
                        System.out.println(msg);
                    }
                } else{
                    System.out.println(message.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }

    }
}

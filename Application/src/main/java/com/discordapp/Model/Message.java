package com.discordapp.Model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Message.
 */
public class Message implements Serializable {
    private String content;
    private String authorName;
    private LocalDateTime date;
    private boolean isFile;
    private int fileSize;
    private byte[] file;
    private HashMap<Reaction, ArrayList<String>> reactions;

    /**
     * Instantiates a new Message.
     *
     * @param content    the content
     * @param authorName the author name
     * @param date       the date
     */
    public Message(String content, String authorName, LocalDateTime date) {
        this.content = content;
        this.authorName = authorName;
        this.date = date;
        this.reactions = new HashMap<>();
    }

    /**
     * Instantiates a new Message.
     *
     * @param content    the content
     * @param authorName the author name
     * @param date       the date
     * @param isFile     the is file
     */
    public Message(String content, String authorName, LocalDateTime date, boolean isFile) {
        this.content = content;
        this.authorName = authorName;
        this.date = date;
        this.reactions = new HashMap<>();
        this.isFile = isFile;
        loadFile(getPath());
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets author name.
     *
     * @return the author name
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Get file byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getFile() {
        return file;
    }

    /**
     * Is file boolean.
     *
     * @return the boolean
     */
    public boolean isFile() {
        return isFile;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
        // file>path
        String[] parts = this.content.split(">");
        String path = parts[1];
        return path;
    }

    /**
     * Gets file name.
     *
     * @return the file name
     */
    public String getFileName() {
        String[] parts = getPath().split("[/\\\\]");
        return parts[parts.length - 1];
    }

    /**
     * Load file.
     *
     * @param path the path
     */
    public void loadFile(String path) {
        try {
            InputStream input = new FileInputStream(path);
            file = input.readAllBytes();
            fileSize = file.length;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Set reaction.
     *
     * @param type the type
     * @param name the name
     */
    public void setReaction(String type, String name) {
        Reaction reaction = new Reaction();
        switch (type) {
            case "like" -> reaction.setLikeReact();
            case "dislike" -> reaction.setDisLikeReact();
            case "smile" -> reaction.setSmileReact();
        }
        addReaction(reaction, name);
    }


    /**
     * Add reaction.
     *
     * @param reaction the reaction
     * @param name     the name
     */
    public void addReaction(Reaction reaction, String name) {
        ArrayList<String> names = reactions.get(reaction);
        if (names == null) {
            names = new ArrayList<>();
        }
        names.add(name);
        reactions.put(reaction, names);
    }

    public int getReactCount(String type) {
        for(Reaction reaction : reactions.keySet()){
            if(reaction.getEmoji().equals(type))
                return reactions.get(reaction).size();
        }
        return 0;
    }


    public String getDate() {
        return date.toString();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern(
                "MMM-dd, HH:mm");
        return String.format("[%s] %s: %s", date.format(formatter), authorName, content);
    }

    public String fileToString() {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern(
                "MMM-dd, HH:mm");
        return String.format("[%s] %s: %s", date.format(formatter), authorName, getFileName());
    }
}

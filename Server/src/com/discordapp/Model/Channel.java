package com.discordapp.Model;

import java.io.Serializable;

/**
 * The type Channel.
 */
public abstract class Channel implements Serializable {
    /**
     * The Name.
     */
    String name;
    /**
     * The Group chat.
     */
    GroupChat groupChat;

    /**
     * Instantiates a new Channel.
     *
     * @param name      the name
     * @param groupChat the group chat
     */
    public Channel(String name, GroupChat groupChat) {
        this.name = name;
        this.groupChat = groupChat;
    }

    /**
     * Instantiates a new Channel.
     *
     * @param name the name
     */
    public Channel(String name) {
        this.name = name;
        this.groupChat = new GroupChat();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets group chat.
     *
     * @return the group chat
     */
    public GroupChat getGroupChat() {
        return groupChat;
    }
}

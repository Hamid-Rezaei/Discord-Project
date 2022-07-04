package com.discordapp.Model;

/**
 * The type Voice channel.
 */
public class VoiceChannel extends Channel {
    /**
     * Instantiates a new Voice channel.
     *
     * @param name      the name
     * @param groupChat the group chat
     */
    public VoiceChannel(String name, GroupChat groupChat) {
        super(name, groupChat);
    }
}

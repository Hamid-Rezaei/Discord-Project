package com.discordapp.Model;

import java.io.Serializable;
import java.util.*;


/**
 * The type Guild.
 */
public class Guild implements Serializable {
    private String name;
    private GuildUser owner;
    private HashSet<GuildUser> guildUsers;
    private ArrayList<TextChannel> textChannels;
    private ArrayList<VoiceChannel> voiceChannels;

    /**
     * Instantiates a new Guild.
     *
     * @param name  the name
     * @param owner the owner
     */
    public Guild(String name, GuildUser owner) {
        this.name = name;
        this.owner = owner;
        guildUsers = new HashSet<>();
        textChannels = new ArrayList<>();
        voiceChannels = new ArrayList<>();
        addUser(owner);
    }

    /**
     * Has user boolean.
     *
     * @param username the username
     * @return the boolean
     */
    public boolean hasUser(String username) {
        for (GuildUser guildUser : guildUsers) {
            if (guildUser.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove member.
     *
     * @param guildUser the guild user
     */
    public void removeMember(GuildUser guildUser){
        guildUsers.remove(guildUser);
    }

    /**
     * Remove member.
     *
     * @param guildUser the guild user
     */
    public void removeMember(String guildUser){
        guildUsers.removeIf(guildUser1 -> guildUser1.getUsername().equals(guildUser));
    }

    /**
     * Gets text channels.
     *
     * @return the text channels
     */
    public ArrayList<TextChannel> getTextChannels() {
        return textChannels;
    }

    /**
     * Gets voice channels.
     *
     * @return the voice channels
     */
    public ArrayList<VoiceChannel> getVoiceChannels() {
        return voiceChannels;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
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
     * Gets owner name.
     *
     * @return the owner name
     */
    public String getOwnerName() {
        return owner.getUsername();
    }

    /**
     * Gets guild users.
     *
     * @return the guild users
     */
    public HashSet<GuildUser> getGuildUsers() {
        return guildUsers;
    }

    /**
     * Add user.
     *
     * @param user the user
     */
    public void addUser(GuildUser user) {
        guildUsers.add(user);
    }

    /**
     * Add text chanel.
     *
     * @param textChannel the text channel
     */
    public void addTextChanel(TextChannel textChannel) {
        textChannels.add(textChannel);
    }

    /**
     * Add voice channel.
     *
     * @param voiceChannel the voice channel
     */
    public void addVoiceChannel(VoiceChannel voiceChannel) {
        voiceChannels.add(voiceChannel);
    }

    /**
     * Remove text channel.
     *
     * @param textChannel the text channel
     */
    public void removeTextChannel(TextChannel textChannel){
        textChannels.remove(textChannel);
    }

    /**
     * Remove text channel.
     *
     * @param textChannel the text channel
     */
    public void removeTextChannel(String textChannel){
        textChannels.removeIf(textChannel1 -> textChannel1.getName().equals(textChannel));
    }

    /**
     * Remove voice channel.
     *
     * @param voiceChannel the voice channel
     */
    public void removeVoiceChannel(VoiceChannel voiceChannel) {
        voiceChannels.remove(voiceChannel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guild)) return false;
        Guild guild = (Guild) o;
        return Objects.equals(getName(), guild.getName()) && Objects.equals(owner, guild.owner) && Objects.equals(getGuildUsers(), guild.getGuildUsers()) && Objects.equals(getTextChannels(), guild.getTextChannels());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), owner, getGuildUsers(), getTextChannels(), getVoiceChannels());
    }

}

package com.discordapp.Model;

import java.io.Serializable;

/**
 * The type Reaction.
 */
public class Reaction implements Serializable {
    /**
     * The Emoji.
     */
    String emoji;

    /**
     * Sets like react.
     */
    public void setLikeReact() {
        emoji = "like";
    }

    /**
     * Sets dis like react.
     */
    public void setDisLikeReact() {
        emoji = "dislike";
    }

    /**
     * Sets smile react.
     */
    public void setSmileReact() {
        emoji = "smile";
    }


    /**
     * Gets emoji.
     *
     * @return the emoji
     */
    public String getEmoji() {
        return emoji;
    }
}

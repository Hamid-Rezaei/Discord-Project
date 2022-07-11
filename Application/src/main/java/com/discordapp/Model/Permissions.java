package com.discordapp.Model;

import java.io.Serializable;
import java.util.Objects;

/**
 * The type Permissions.
 */
public class Permissions implements Serializable {
    private boolean CAN_CREATE_CHANNEL = false;

    private boolean CAN_DELETE_CHANNEL = false;

    private boolean CAN_REMOVE_USER = false;

    private boolean CAN_STRICT_CHANNEL = false;

    private boolean CAN_CHANGE_SERVERNAME = false;

    private boolean CAN_SEE_HISTORY = true;

    private boolean CAN_PIN_MESSAGE = false;

    private boolean CAN_BAN_USER = false;

    private boolean CAN_ADD_USER = true;


    /**
     * Sets can create channel.
     *
     * @param CAN_CREATE_CHANNEL the can create channel
     */
    public void setCAN_CREATE_CHANNEL(boolean CAN_CREATE_CHANNEL) {
        this.CAN_CREATE_CHANNEL = CAN_CREATE_CHANNEL;
    }

    /**
     * Sets can delete channel.
     *
     * @param CAN_DELETE_CHANNEL the can delete channel
     */
    public void setCAN_DELETE_CHANNEL(boolean CAN_DELETE_CHANNEL) {
        this.CAN_DELETE_CHANNEL = CAN_DELETE_CHANNEL;
    }

    /**
     * Sets can remove user.
     *
     * @param CAN_REMOVE_USER the can remove user
     */
    public void setCAN_REMOVE_USER(boolean CAN_REMOVE_USER) {
        this.CAN_REMOVE_USER = CAN_REMOVE_USER;
    }

    /**
     * Sets can strict channel.
     *
     * @param CAN_STRICT_CHANNEL the can strict channel
     */
    public void setCAN_STRICT_CHANNEL(boolean CAN_STRICT_CHANNEL) {
        this.CAN_STRICT_CHANNEL = CAN_STRICT_CHANNEL;
    }

    /**
     * Sets can change servername.
     *
     * @param CAN_CHANGE_SERVERNAME the can change servername
     */
    public void setCAN_CHANGE_SERVERNAME(boolean CAN_CHANGE_SERVERNAME) {
        this.CAN_CHANGE_SERVERNAME = CAN_CHANGE_SERVERNAME;
    }

    /**
     * Sets can see history.
     *
     * @param CAN_SEE_HISTORY the can see history
     */
    public void setCAN_SEE_HISTORY(boolean CAN_SEE_HISTORY) {
        this.CAN_SEE_HISTORY = CAN_SEE_HISTORY;
    }

    /**
     * Sets can pin message.
     *
     * @param CAN_PIN_MESSAGE the can pin message
     */
    public void setCAN_PIN_MESSAGE(boolean CAN_PIN_MESSAGE) {
        this.CAN_PIN_MESSAGE = CAN_PIN_MESSAGE;
    }

    /**
     * Sets can ban user.
     *
     * @param CAN_BAN_USER the can ban user
     */
    public void setCAN_BAN_USER(boolean CAN_BAN_USER) {
        this.CAN_BAN_USER = CAN_BAN_USER;
    }

    /**
     * Sets can add user.
     *
     * @param CAN_ADD_USER the can add user
     */
    public void setCAN_ADD_USER(boolean CAN_ADD_USER) {
        this.CAN_ADD_USER = CAN_ADD_USER;
    }

    /**
     * Is can add user boolean.
     *
     * @return the boolean
     */
    public boolean isCAN_ADD_USER() {
        return CAN_ADD_USER;
    }

    /**
     * Is can create channel boolean.
     *
     * @return the boolean
     */
    public boolean isCAN_CREATE_CHANNEL() {
        return CAN_CREATE_CHANNEL;
    }

    /**
     * Is can delete channel boolean.
     *
     * @return the boolean
     */
    public boolean isCAN_DELETE_CHANNEL() {
        return CAN_DELETE_CHANNEL;
    }

    /**
     * Is can remove user boolean.
     *
     * @return the boolean
     */
    public boolean isCAN_REMOVE_USER() {
        return CAN_REMOVE_USER;
    }

    /**
     * Is can strict channel boolean.
     *
     * @return the boolean
     */
    public boolean isCAN_STRICT_CHANNEL() {
        return CAN_STRICT_CHANNEL;
    }

    /**
     * Is can change servername boolean.
     *
     * @return the boolean
     */
    public boolean isCAN_CHANGE_SERVERNAME() {
        return CAN_CHANGE_SERVERNAME;
    }

    /**
     * Is can see history boolean.
     *
     * @return the boolean
     */
    public boolean isCAN_SEE_HISTORY() {
        return CAN_SEE_HISTORY;
    }

    /**
     * Is can pin message boolean.
     *
     * @return the boolean
     */
    public boolean isCAN_PIN_MESSAGE() {
        return CAN_PIN_MESSAGE;
    }

    /**
     * Is can ban user boolean.
     *
     * @return the boolean
     */
    public boolean isCAN_BAN_USER() {
        return CAN_BAN_USER;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permissions)) return false;
        Permissions that = (Permissions) o;
        return isCAN_CREATE_CHANNEL() == that.isCAN_CREATE_CHANNEL() && isCAN_DELETE_CHANNEL() == that.isCAN_DELETE_CHANNEL() && isCAN_REMOVE_USER() == that.isCAN_REMOVE_USER() && isCAN_STRICT_CHANNEL() == that.isCAN_STRICT_CHANNEL() && isCAN_CHANGE_SERVERNAME() == that.isCAN_CHANGE_SERVERNAME() && isCAN_SEE_HISTORY() == that.isCAN_SEE_HISTORY() && isCAN_PIN_MESSAGE() == that.isCAN_PIN_MESSAGE() && isCAN_BAN_USER() == that.isCAN_BAN_USER() && isCAN_ADD_USER() == that.isCAN_ADD_USER();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isCAN_CREATE_CHANNEL(), isCAN_DELETE_CHANNEL(), isCAN_REMOVE_USER(), isCAN_STRICT_CHANNEL(), isCAN_CHANGE_SERVERNAME(), isCAN_SEE_HISTORY(), isCAN_PIN_MESSAGE(), isCAN_BAN_USER(), isCAN_ADD_USER());
    }
}

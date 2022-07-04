package com.discordapp.Model;

/**
 * The enum Permission.
 */
public enum Permission {
    /**
     * Can create channel permission.
     */
    CAN_CREATE_CHANNEL(false),

    /**
     * Can delete channel permission.
     */
    CAN_DELETE_CHANNEL(false),

    /**
     * Can remove user permission.
     */
    CAN_REMOVE_USER(false),

    /**
     * Can strict channel permission.
     */
    CAN_STRICT_CHANNEL(false),

    /**
     * Can change servername permission.
     */
    CAN_CHANGE_SERVERNAME(false),

    /**
     * Can see history permission.
     */
    CAN_SEE_HISTORY(true),

    /**
     * Can pin message permission.
     */
    CAN_PIN_MESSAGE(false),

    /**
     * Can ban user permission.
     */
    CAN_BAN_USER(false);

    /**
     * The Is able.
     */
    public boolean isAble;

    private Permission(boolean isAble) {
        this.isAble = isAble;
    }

    /**
     * Is able boolean.
     *
     * @return the boolean
     */
    public boolean isAble() {
        return isAble;
    }
}

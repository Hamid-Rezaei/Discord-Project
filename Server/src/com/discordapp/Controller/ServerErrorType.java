package com.discordapp.Controller;

/**
 * The enum Server error type.
 */
public enum ServerErrorType {
    /**
     * No error server error type.
     */
    NO_ERROR(1),
    /**
     * User already exists server error type.
     */
    USER_ALREADY_EXISTS(2),
    /**
     * Server connection failed server error type.
     */
    SERVER_CONNECTION_FAILED(3),
    /**
     * com.discordapp.Database error server error type.
     */
    DATABASE_ERROR(4),
    /**
     * Duplicate error server error type.
     */
    Duplicate_ERROR(5),
    /**
     * Already friend server error type.
     */
    ALREADY_FRIEND(6),
    /**
     * Unknown error server error type.
     */
    UNKNOWN_ERROR(404);


    private final int code;

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    ServerErrorType(int code) {
        this.code = code;
    }
}

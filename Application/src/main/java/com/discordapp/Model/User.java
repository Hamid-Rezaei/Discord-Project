package com.discordapp.Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;


/**
 * The type User.
 */
public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Status status;
    private String token;
    private byte[] avatar;
    private ArrayList<User> friends;
    private ArrayList<String> friendRequests;


    /**
     * Instantiates a new User.;
     *
     * @param username    the username
     * @param password    the password
     * @param email       the email
     * @param phoneNumber the phone number
     * @param token       the token
     * @param avatar      the avatar
     */
    public User(String username, String password, String email, String phoneNumber, String token, byte[] avatar) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = Status.ONLINE;
        this.token = token;
        friends = new ArrayList<>();
        this.avatar = avatar;
    }

    /**
     * Instantiates a new User.
     *
     * @param username    the username
     * @param password    the password
     * @param email       the email
     * @param phoneNumber the phone number
     * @param token       the token
     * @param avatar      the avatar
     * @param status      the status
     */
    public User(String username, String password, String email, String phoneNumber, String token, byte[] avatar,Status status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.token = token;
        friends = new ArrayList<>();
        this.avatar = avatar;
    }

    /**
     * Get avatar byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getAvatar() {
        return avatar;
    }


    /**
     * Sets avatar.
     *
     * @param avatar the avatar
     * @throws IOException the io exception
     */
    public void setAvatar(byte[] avatar) throws IOException {
        this.avatar =avatar;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Change password.
     *
     * @param password the password
     */
    public void changePassword(String password) {
        this.password = password;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPhoneNumber(), user.getPhoneNumber()) && getStatus() == user.getStatus() && Objects.equals(getToken(), user.getToken()) && Objects.equals(getAvatar(), user.getAvatar()) && Objects.equals(friends, user.friends) && Objects.equals(friendRequests, user.friendRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getEmail(), getPhoneNumber(), getStatus(), getToken(), getAvatar(), friends, friendRequests);
    }

    @Override
    public String toString() {
        return status + username + Status.RESET;
    }
}

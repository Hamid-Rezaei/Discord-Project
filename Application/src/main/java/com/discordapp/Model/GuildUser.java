package com.discordapp.Model;


import java.io.Serializable;

/**
 * The type Guild user.
 */
public class GuildUser extends User implements Serializable {

    private Role role;

    /**
     * Instantiates a new Guild user.
     *
     * @param user the user
     * @param role the role
     */
    public GuildUser(User user, Role role) {
        super(user.getUsername(), user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getToken(), user.getAvatar());
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString(){
        return super.toString();
    }
}

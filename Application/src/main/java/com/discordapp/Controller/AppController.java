package com.discordapp.Controller;

import com.discordapp.Model.*;
import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


/**
 * The type App controller.
 */
public class AppController {
    Scanner sc = new Scanner(System.in);


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
         * Database error server error type.
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

        private int code;

        ServerErrorType(int code) {
            this.code = code;
        }
    }

    private User currentUser;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    /**
     * Instantiates a new App controller.
     */
    public AppController() {
        connect();
    }


    public void connect() {
        try {
            if (socket == null || !socket.isConnected()) {
                socket = new Socket("localhost", 7777);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
            }
        } catch (IOException x) {
            System.out.println("SERVER CONNECTION FAILED.");
            System.exit(0);
        }
    }

    public void disconnect() {
        try {
            if (socket.isConnected()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sign up.
     *
     * @param username the username
     * @param password the password
     * @param email    the email
     * @param phoneNum the phone num
     * @param avatar   the avatar
     * @return the string
     */
    public String signUp(String username, String password, String email, String phoneNum, InputStream avatar) {

        try {
            outputStream.writeUTF("#signUp");
            outputStream.flush();
            outputStream.writeUTF(username + " " + password + " " + email + " " + phoneNum);
            outputStream.flush();
            byte[] img = avatar.readAllBytes();
            outputStream.writeInt(img.length);
            outputStream.flush();
            outputStream.write(img, 0, img.length);
            outputStream.reset();
            return parseError(inputStream.readInt());
        } catch (IOException x) {
            x.printStackTrace();
            return "IOException";
        }
    }

    public String signUp(String username, String password, String email) {

        try {
            outputStream.writeUTF("#signUpNew");
            outputStream.flush();
            outputStream.writeUTF(username + " " + password + " " + email + " " + "");
            outputStream.flush();
            outputStream.reset();
            return parseError(inputStream.readInt());
        } catch (IOException x) {
            x.printStackTrace();
            return "IOException";
        }
    }


    /**
     * Login user.
     *
     * @param username the username
     * @param password the password
     * @return the user
     */
    public User login(String username, String password) {
        try {
            outputStream.writeUTF("#login");
            outputStream.flush();
            outputStream.writeUTF(username + " " + password);
            outputStream.flush();
            User user = (User) inputStream.readObject();
            if (user == null) {
                return user;
            }
//            String name = inputStream.readUTF();
//            String pass = inputStream.readUTF();
//            String email = inputStream.readUTF();
//            String phoneNum = inputStream.readUTF();
//            String token = inputStream.readUTF();
            int avatarSize = inputStream.readInt();
            byte[] avatar = new byte[avatarSize];
            inputStream.readFully(avatar, 0, avatarSize);
            //User user = new User(name, pass, email, phoneNum, token, avatar, Status.ONLINE);
            user.setAvatar(avatar);
            currentUser = user;
            return user;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can not write for server.");
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Can not read from server.");
            return null;
        }
    }

    /**
     * Sets status.
     *
     * @param status   the status
     * @param username the username
     * @return the status
     */
    public String setStatus(String status, String username) {
        try {
            outputStream.writeUTF("#setStatus");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            outputStream.writeUTF(status);
            outputStream.flush();
            String respond = inputStream.readUTF();
            return respond;
        } catch (IOException e) {
            e.printStackTrace();
            return "couldn't change status.";
        }
    }

    /**
     * Friend request.
     *
     * @param username   the username
     * @param targetUser the target user
     * @return the string
     */
    public String friendRequest(String username, String targetUser) {
        String answer;
        int answerCode;
        try {
            outputStream.writeUTF("#friendRequest");
            outputStream.flush();

            outputStream.writeUTF(username);
            outputStream.flush();

            outputStream.writeUTF(targetUser);
            outputStream.flush();
            answerCode = inputStream.readInt();
            answer = parseError(answerCode);
        } catch (IOException e) {
            e.printStackTrace();
            answer = "something went wrong with friend Request.";
        }
        return answer;
    }

    /**
     * Friend request list hash set.
     *
     * @param username the username
     * @return the hash set
     */
    public ArrayList<User> friendRequestList(String username) {
        try {
            outputStream.writeUTF("#RequestList");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            HashSet<User> names = (HashSet<User>) inputStream.readObject();
            return new ArrayList<>(names);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Revised friend requests.
     *
     * @param username the username
     * @param accepted the accepted
     * @param rejected the rejected
     * @return the string
     */
    public String revisedFriendRequests(String username, HashSet<String> accepted, HashSet<String> rejected) {
        try {
            outputStream.writeUTF("#revisedFriendRequests");
            outputStream.flush();
            outputStream.writeObject(accepted);
            outputStream.flush();
            outputStream.writeObject(rejected);
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            String response = inputStream.readUTF();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "something went wrong while revising Friend requests.";
        }
    }

    /**
     * Friend list hash set.
     *
     * @param username the username
     * @return the hash set
     */
    public HashSet<User> friendSet(String username) {
        try {
            outputStream.writeUTF("#FriendList");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            return (HashSet<User>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<User> friendList(String username) {
        try {
            outputStream.writeUTF("#FriendList");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            HashSet<User> friendSet = (HashSet<User>) inputStream.readObject();
            return new ArrayList<User>(friendSet);
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<User> friends(String username) {
        HashSet<User> friendList = friendSet(username);
        ArrayList<User> friends = friendList(username);
//        for (String friend : friendList) {
//            friends.add(getUser(friend));
//        }
        return friends;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public User getUser(String username) {
        try {
            outputStream.writeUTF("#getUser");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            User user = (User) inputStream.readUnshared();
            return user;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Block user.
     *
     * @param username the username
     * @return the string
     */
    public String blockUser(String username) {
        try {
            outputStream.writeUTF("#blockUser");
            outputStream.writeUTF(currentUser.getUsername());
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            String dbResponse = inputStream.readUTF();
            return dbResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return "something went wrong while blocking user.";
        }
    }

    /**
     * Unblock user.
     *
     * @param unblockTarget the unblock target
     * @return the string
     */
    public void unblockUser(String unblockTarget) {
        try {
            outputStream.writeUTF("#unblockUser");
            outputStream.flush();
            outputStream.writeUTF(currentUser.getUsername());
            outputStream.flush();
            outputStream.writeUTF(unblockTarget);
            outputStream.flush();
            String respone = inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }
    }


    /**
     * Blocked list hash set.
     *
     * @return the hash set
     */
    public ArrayList<User> blockedList(String username) {
        try {
            outputStream.writeUTF("#blockList");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            HashSet<User> blockedList = (HashSet<User>) inputStream.readObject();
            return new ArrayList<>(blockedList);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Request for direct chat.
     *
     * @param friend the friend
     */
    public void requestForDirectChat(User friend) {
        try {
            outputStream.writeUTF("#requestForDirectChat");
            outputStream.flush();
            outputStream.writeObject(friend);
            outputStream.flush();
            outputStream.writeObject(currentUser);
            outputStream.flush();
            String answer = inputStream.readUTF();
            if (answer.equals("Success")) {
                Chat directChat = (Chat) inputStream.readObject();
                directChat.setCurrUser(currentUser);
                directChat.setOutputStream(outputStream);
                directChat.setInputStream(inputStream);
                Thread chatThread = new Thread(directChat);
                chatThread.start();
                chatThread.join();
            }

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }

    public ArrayList<Message> loadDirectChatMessages(String user, String friend) {
        try {
            outputStream.writeUTF("#getDirectChatMessages");
            outputStream.flush();
            outputStream.writeUTF(user);
            outputStream.flush();
            outputStream.writeUTF(friend);
            outputStream.flush();
            ArrayList<Message> messages = (ArrayList<Message>) inputStream.readObject();
            return messages;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void sendMessageToDirectChat(String username, String friendName, Message message) {
        try {
            outputStream.writeUTF("#sendMsgToDirectChat");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            outputStream.writeUTF(friendName);
            outputStream.flush();
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessageToGroupChat(String owner, String guildName, String textChannelName, Message message) {
        try {
            outputStream.writeUTF("#messageToGroupChat");
            outputStream.flush();
            outputStream.writeUTF(owner);
            outputStream.flush();
            outputStream.writeUTF(guildName);
            outputStream.flush();
            outputStream.writeUTF(textChannelName);
            outputStream.flush();
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Remove from direct chat.
     *
     * @param user   the user
     * @param friend the friend
     */
    public void removeFromDirectChat(User user, User friend) {
        try {
            outputStream.writeUTF("#removeFromChat");
            outputStream.flush();
            outputStream.writeUTF(user.getUsername());
            outputStream.flush();
            outputStream.writeUTF(friend.getUsername());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add server.
     *
     * @param guild the guild
     * @return the string
     */
    public String addServer(Guild guild) {
        try {
            outputStream.writeUTF("#addGuild");
            outputStream.flush();
            outputStream.writeObject(guild);
            outputStream.flush();
            return inputStream.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
            return "Fail to add server.";
        }
    }

    /**
     * List of joined servers array list.
     *
     * @return the array list
     */
    public ArrayList<Guild> listOfJoinedServers(String username) {
        try {
            outputStream.writeUTF("#serverList");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            ArrayList<Guild> guilds = (ArrayList<Guild>) inputStream.readUnshared();
            return guilds;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets guild.
     *
     * @param owner     the owner
     * @param guildName the guild name
     * @return the guild
     */
    public Guild getGuild(String owner, String guildName) {
        try {
            outputStream.writeUTF("#getGuild");
            outputStream.flush();
//            outputStream.writeUTF(owner);
//            outputStream.flush();
            outputStream.writeUTF(guildName);
            outputStream.flush();
            Guild guild = (Guild) inputStream.readUnshared();
            return guild;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Add member to server .
     *
     * @param name  the name
     * @param guild the guild
     * @return the string
     */
    public String addMemberToServer(String name, Guild guild) {
        try {
            User user = getUser(name);
            GuildUser member = new GuildUser(user, new Role("member"));
            outputStream.writeUTF("#addMember");
            outputStream.flush();
            outputStream.writeObject(member);
            outputStream.flush();
            outputStream.writeUTF(guild.getOwnerName());
            outputStream.flush();
            outputStream.writeUTF(guild.getName());
            outputStream.flush();
            String respond = inputStream.readUTF();
            return respond;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "something went wrong while adding member to server.";
    }

    /**
     * Add new text channel .
     *
     * @param guild the guild
     * @return the string
     */
    public String addNewTextChannel(Guild guild, String channelName) {
        String response = null;
        try {
            outputStream.writeUTF("#addTextChannel");
            outputStream.flush();
            outputStream.writeUTF(channelName);
            outputStream.flush();
            outputStream.writeUTF(guild.getOwnerName());
            outputStream.flush();
            outputStream.writeUTF(guild.getName());
            outputStream.flush();
            response = inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            response = "something went wrong while adding text channel";
        }
        return response;
    }

    /**
     * Request for group chat.
     *
     * @param guild       the guild
     * @param textChannel the text channel
     */
    public ArrayList<Message> getTextChannelMessages(Guild guild, TextChannel textChannel) {
        try {
            outputStream.writeUTF("#getTextChannelMsg");
            outputStream.flush();
            outputStream.writeUTF(guild.getOwnerName());
            outputStream.flush();
            outputStream.writeUTF(guild.getName());
            outputStream.flush();
            outputStream.writeUTF(textChannel.getName());
            outputStream.flush();

            String answer = inputStream.readUTF();
            if (answer.equals("success.")) {
                ArrayList<Message> messages = (ArrayList<Message>) inputStream.readObject();
                return messages;
            } else {
                System.out.println("something went wrong while requesting for group chat.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
        return new ArrayList<>();
    }

    /**
     * Delete member from server.
     *
     * @param name  the name
     * @param guild the guild
     * @return the string
     */
    public String deleteMemberFromServer(String name, Guild guild) {
        try {
            User user = getUser(name);
            GuildUser member = new GuildUser(user, new Role("member"));
            outputStream.writeUTF("#removeMember");
            outputStream.flush();
            outputStream.writeObject(member);
            outputStream.flush();
            outputStream.writeUTF(guild.getOwnerName());
            outputStream.flush();
            outputStream.writeUTF(guild.getName());
            outputStream.flush();
            String respond = inputStream.readUTF();
            return respond;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "something went wrong while removing member from server.";
    }

    /**
     * Update user .
     *
     * @param user the user
     * @return the boolean
     */
    public boolean updateUser(User user) {
        try {
            outputStream.writeUTF("#updateUser");
            outputStream.flush();
            outputStream.writeObject(user);
            outputStream.flush();
            outputStream.reset();
            String respone = inputStream.readUTF();
            if (respone.equals("success.")) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Change guild name.
     *
     * @param guild   the guild
     * @param newName the new name
     * @return the string
     */
    public String changeGuildName(Guild guild, String newName) {
        try {
            outputStream.writeUTF("#changeGuildName");
            outputStream.flush();
            outputStream.writeUTF(guild.getOwnerName());
            outputStream.flush();
            outputStream.writeUTF(guild.getName());
            outputStream.flush();
            outputStream.writeUTF(newName);
            outputStream.flush();
            return inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "something went wrong while renaming server.";
    }

    /**
     * Remove text channel.
     *
     * @param guild       the guild
     * @param textChannel the text channel
     * @return the string
     */
    public String removeTextChannel(Guild guild, String textChannel) {
        String response = null;
        try {
            outputStream.writeUTF("#deleteTextChannel");
            outputStream.flush();
            outputStream.writeUTF(guild.getOwnerName());
            outputStream.flush();
            outputStream.writeUTF(guild.getName());
            outputStream.flush();
            outputStream.writeUTF(textChannel);
            outputStream.flush();
            response = inputStream.readUTF();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "couldn't delete text channel.";
        }
    }

    /**
     * Delete guild.
     *
     * @param guild  the guild
     * @param gOwner the g owner
     * @return the string
     */
    public String deleteGuild(Guild guild, String gOwner) {
        try {
            outputStream.writeUTF("#deleteGuild");
            outputStream.flush();
            outputStream.writeUTF(gOwner);
            outputStream.flush();
            outputStream.writeObject(guild);
            outputStream.flush();
            return inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return "Oops, Something went wong we can not delete server, please try again.";
        }
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ArrayList<Message> getPinnedMessages(String username, String friendName) {
        try {
            outputStream.writeUTF("#getPinnedMessages");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            outputStream.writeUTF(friendName);
            outputStream.flush();
            ArrayList<Message> pinnedMessages = (ArrayList<Message>) inputStream.readObject();
            return pinnedMessages;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public ArrayList<Message> getGroupChatPinnedMessages(String gOwner, String guildName, String textChannelName) {
        try {
            outputStream.writeUTF("#getGroupChatPinnedMessages");
            outputStream.flush();
            outputStream.writeUTF(gOwner);
            outputStream.flush();
            outputStream.writeUTF(guildName);
            outputStream.flush();
            outputStream.writeUTF(textChannelName);
            outputStream.flush();
            ArrayList<Message> pinnedMessages = (ArrayList<Message>) inputStream.readObject();
            return pinnedMessages;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    /**
     * Parse error.
     *
     * @param errorCode the error code
     * @return the string
     */
    public String parseError(int errorCode) {
        String error;
        switch (errorCode) {
            case 1:
                error = "Success.";
                break;
            case 2:
                error = "This user already exists.";
                break;
            case 3:
                error = "Connection with server failed.";
                break;
            case 4:
                error = "There was a problem with database.";
                break;
            case 5:
                error = "this user doesnt exist or you already have a friend request with them.";
                break;
            case 6:
                error = "you are already friend of this user.";
                break;
            default:
                error = "UNKNOWN ERROR.";
                break;

        }
        return error;
    }


    public boolean isConnected() {
        return socket.isConnected();
    }
}
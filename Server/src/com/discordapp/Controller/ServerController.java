package com.discordapp.Controller;


import com.discordapp.Database.Database;
import com.discordapp.Model.Message;
import com.discordapp.Model.Status;
import com.discordapp.Model.User;
import com.discordapp.Model.Guild;
import com.discordapp.Model.GuildUser;
import com.discordapp.Model.TextChannel;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The type Server com.discordapp.controller.
 */
public class ServerController implements Runnable {

    /**
     * The constant serverControllers.
     */
    public static ArrayList<ServerController> serverControllers = new ArrayList<>();
    /**
     * The constant directChats.
     */
    public static HashMap<String, DirectChatController> directChats = new HashMap<>();
    /**
     * The constant connections.
     */
    public static HashMap<String, Connection> connections = new HashMap<>();
    /**
     * The constant allGuilds.
     */
    public static HashMap<String, ArrayList<Guild>> allGuilds = new HashMap<>();

    private Socket socket;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String appUsername;

    private String userToken;

    /**
     * Instantiates a new Server com.discordapp.controller.
     *
     * @param socket the socket
     * @throws IOException the io exception
     */
    public ServerController(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        serverControllers.add(this);
    }


    /**
     * calls the function that matches with given input from user service.
     */
    public void getService() {
        try {
            String task = inputStream.readUTF();
            switch (task) {
                case "#signUp" -> signUp();
                case "#login" -> login();
                case "#friendRequest" -> friendRequest();
                case "#RequestList" -> friendRequestList();
                case "#FriendList" -> friendList();
                case "#getUser" -> getUserWithUsername();
                case "#requestForDirectChat" -> chatWithFriend();
                case "#revisedFriendRequests" -> revisedFriendRequests();
                case "#addGuild" -> addGuild();
                case "#getGuild" -> getGuild();
                case "#serverList" -> listOfUserServers();
                case "#blockUser" -> blockUser();
                case "#blockList" -> blockList();
                case "#unblockUser" -> unblockUser();
                case "#addMember" -> addMemberToServer();
                case "#addTextChannel" -> addTextChannel();
                case "#removeMember" -> deleteMemberToServer();
                case "#changeGuildName" -> changeGuildName();
                case "#removeFromChat" -> removeFromDirectChat();
                case "#getTextChannel" -> getTextChannel();
                case "#updateUser" -> updateUser();
                case "#setStatus" -> changeStatus();
                case "#deleteGuild" -> deleteGuild();
                case "#deleteTextChannel" -> deleteTextChannel();
                case "#signUpNew" -> signUpNew();
                case "#sendMsgToDirectChat" -> messageToDirectChat();
                case "#getDirectChatMessages" -> getDirectChatMessages();
                case "#getPinnedMessages" -> getPinnedMessages();
            }

        } catch (IOException e) {
            System.out.println("A user disconnected.");
            if (appUsername != null) {
                Object user = Database.retrieveFromDB(appUsername);
                if (user instanceof User)
                    ((User) user).setStatus(Status.INVISIBLE);
                Database.updateUser((User) user);
            }
            try {
                inputStream.close();
                outputStream.close();
                this.socket.close();
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }


    private void messageToDirectChat() {
        try {
            String user = inputStream.readUTF();
            String friend = inputStream.readUTF();
            User currUser = Database.retrieveFromDB(user);
            User friendUser = Database.retrieveFromDB(friend);

            DirectChatController directChatController = getDirChatController(currUser, friendUser);
            Message message = (Message) inputStream.readObject();
            if (message.getContent().equals("#exit")) {
                directChatController.broadcastExitMessage("you exited the chat", connections.get(currUser.getUsername()));
                directChatController.removeConnection(currUser.getUsername());
            } else if (message.getContent().split(">")[0].equals("#pin")) {
                int index = Integer.parseInt(message.getContent().split(">")[1]);
                directChatController.pinMessage(index);
            } else if (message.getContent().split(">")[0].equals("#react")) {
                int index = Integer.parseInt(message.getContent().split(">")[1]);
                String reactionType = message.getContent().split(">")[2].toLowerCase();
                String reactor = message.getAuthorName();
                directChatController.reactToMessage(index, reactionType, reactor);
            } else {
                directChatController.addMessage(message);
                directChatController.broadcastMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getDirectChatMessages() {
        try {
            String username = inputStream.readUTF();
            String friendName = inputStream.readUTF();
            User user = Database.retrieveFromDB(username);
            User friend = Database.retrieveFromDB(friendName);
            outputStream.writeObject(getDirChatController(user, friend).getMessages());
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getPinnedMessages() {
        try {
            String username = inputStream.readUTF();
            String friendName = inputStream.readUTF();
            User user = Database.retrieveFromDB(username);
            User friend = Database.retrieveFromDB(friendName);
            outputStream.writeObject(getDirChatController(user, friend).getPinnedMessages());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void signUpNew() {
        try {
            String[] parts = inputStream.readUTF().split(" ");
            String username = parts[0];
            String pass = parts[1];
            String email = parts[2];
            String phoneNum = null;
            if (parts.length == 4)
                phoneNum = parts[3];
            String token = UUID.randomUUID().toString();
            FileInputStream av = new FileInputStream("./assets/defaultAvatar.png");
            byte[] avatar = av.readAllBytes();
            int answer = Database.insertToDB(username, pass, email, phoneNum, token, avatar).getCode();
            outputStream.writeInt(answer);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * deletes a text channel in a guild
     */
    private void deleteTextChannel() {
        try {
            String gOwner = inputStream.readUTF();
            String gName = inputStream.readUTF();
            TextChannel textChannel = (TextChannel) inputStream.readObject();
            getGuild(gOwner, gName).removeTextChannel(textChannel.getName());
            saveGuilds();
            outputStream.writeUTF("TextChannel " + textChannel.getName() + "removed successfully.");
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * deletes a guild
     */
    private void deleteGuild() {
        try {
            String gOwner = inputStream.readUTF();
            Guild targetGuild = (Guild) inputStream.readObject();
            ArrayList<Guild> guilds = allGuilds.get(gOwner);
            guilds.removeIf(guild -> guild.getName().equals(targetGuild.getName()));
            allGuilds.put(gOwner, guilds);
            saveGuilds();
            outputStream.writeUTF("Guild deleted successfully.");
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * changes a user status
     */
    private void changeStatus() {
        try {
            String username = inputStream.readUTF();
            String statusName = inputStream.readUTF();
            Status stauts = Status.makeStatus(statusName);
            User user = Database.retrieveFromDB(username);
            user.setStatus(stauts);
            Database.updateUser(user);
            outputStream.writeUTF("your new status is: " + stauts.toString(stauts));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sign up.
     */
    public void signUp() {
        try {
            String[] parts = inputStream.readUTF().split(" ");
            String username = parts[0];
            String pass = parts[1];
            String email = parts[2];
            String phoneNum = null;
            if (parts.length == 4)
                phoneNum = parts[3];
            String token = UUID.randomUUID().toString();
            int avatarSize = inputStream.readInt();
            byte[] avatar = new byte[avatarSize];
            inputStream.readFully(avatar, 0, avatarSize);
            int answer = Database.insertToDB(username, pass, email, phoneNum, token, avatar).getCode();
            outputStream.writeInt(answer);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Login.
     */
    public void login() {
        try {
            String[] parts = inputStream.readUTF().split(" ");
            if (parts.length < 2) {
                outputStream.writeObject(null);
                outputStream.flush();
                return;
            }
            String username = parts[0];
            String pass = parts[1];
            User answer = Database.retrieveFromDB(username, pass);
            if (answer == null) {
                outputStream.writeObject(answer);
                outputStream.flush();
                return;
            }

            outputStream.writeObject(answer);
            outputStream.flush();
            byte[] byteAvatar = ((User) answer).getAvatar();
            answer.setStatus(Status.ONLINE);
            Database.updateUser((User) answer);
//            outputStream.writeObject(answer);
//            outputStream.flush();

            outputStream.writeInt(byteAvatar.length);
            outputStream.write(byteAvatar, 0, byteAvatar.length);
            outputStream.flush();
            outputStream.reset();
            appUsername = answer.getUsername();
            Connection connection = new Connection(this.socket, outputStream, inputStream, this.appUsername);
            connections.put(appUsername, connection);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Update user.
     */
    public void updateUser() {
        try {
            User user = (User) inputStream.readObject();
            boolean updated = Database.updateUser(user);
            if (updated) {
                outputStream.writeUTF("success.");
                outputStream.flush();
            } else {
                outputStream.writeUTF("failed.");
                outputStream.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends friend request.
     */
    public void friendRequest() {
        try {
            String fromUser = inputStream.readUTF();
            String targetUser = inputStream.readUTF();
            ServerErrorType answer = Database.sendFriendRequest(fromUser, targetUser);
            outputStream.writeInt(answer.getCode());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends list of friend requests
     */
    private void friendRequestList() {
        try {
            String username = inputStream.readUTF();
            HashSet<User> reqList = Database.viewFriendRequestList(username);
            outputStream.writeObject(reqList);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * updates friend requests list
     */
    private void revisedFriendRequests() {
        try {
            HashSet<String> accepted = (HashSet<String>) inputStream.readObject();
            HashSet<String> rejected = (HashSet<String>) inputStream.readObject();
            String username = inputStream.readUTF();
            String answer = Database.reviseFriendRequests(username, accepted, rejected);
            outputStream.writeUTF(answer);
            outputStream.flush();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends Friend list.
     */
    public void friendList() {
        try {
            String username = inputStream.readUTF();
            HashSet<User> friendList = Database.viewFriendList(username);
            outputStream.writeObject(friendList);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns a user with just username
     */
    private void getUserWithUsername() {
        try {
            String username = inputStream.readUTF();
            User friend = Database.retrieveFromDB(username);
            outputStream.writeUnshared(friend);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Direct chat hash code string.
     *
     * @param user_1 the user 1
     * @param user_2 the user 2
     * @return the string
     */
    public String directChatHashCode(String user_1, String user_2) {
        String hash;
        if (user_1.length() < user_2.length()) {
            hash = user_1 + user_2;
        } else {
            hash = user_2 + user_1;
        }
        return hash;
    }

    /**
     * creates/gets a direct chat between two friends
     *
     * @param currentUser user that wants to enter a direct chat
     * @param friend      second user of the direct chat
     * @return direct chat
     */
    private DirectChatController getDirChatController(User currentUser, User friend) {
        String chatHash = directChatHashCode(currentUser.getUsername(), friend.getUsername());
        DirectChatController directChatController = directChats.get(chatHash);
        if (directChatController == null) {
            Connection userConnection = connections.get(currentUser.getUsername());
            ArrayList<User> participants = new ArrayList<>();
            participants.add(currentUser);
            participants.add(friend);

            HashSet<Connection> directChatConnections = new HashSet<>();
            directChatConnections.add(userConnection);

            directChatController = new DirectChatController(directChatConnections, participants);
            directChatController.loadMessages();
            directChats.put(directChatController.getChatHashCode(), directChatController);
        }
        directChatController.addConnection(connections.get(currentUser.getUsername()));

        return directChatController;
    }

    /**
     * handles Direct chat with a friend
     */
    private void chatWithFriend() {
        try {
            User friend = (User) inputStream.readObject();
            User currentUser = (User) inputStream.readObject();
            DirectChatController directChatController = getDirChatController(currentUser, friend);
            Thread directChat = new Thread(directChatController, directChatController.getChatHashCode());
            directChat.start();
            outputStream.writeUTF("Success");
            outputStream.flush();
            outputStream.writeObject(directChatController.getDirectChat());
            outputStream.flush();
            directChatController.broadcastMessages(connections.get(currentUser.getUsername()));
            Message message = null;
            boolean inChat = true;
            while (inChat) {
                Object obj = inputStream.readObject();
                if (obj instanceof Message) {
                    message = (Message) obj;
                    if (message.getContent().equals("#exit")) {
                        directChatController.broadcastExitMessage("you exited direct chat.", connections.get(currentUser.getUsername()));
                        directChatController.removeConnection(currentUser.getUsername());
                        break;
                    } else if (message.getContent().split(">")[0].equals("#pin")) {
                        int index = Integer.parseInt(message.getContent().split(">")[1]);
                        directChatController.pinMessage(index);
                        directChatController.broadcastExitMessage("message pinned successfully.", connections.get(currentUser.getUsername()));
                    } else if (message.getContent().equals("#pins")) {
                        directChatController.showPinnedMessages(connections.get(currentUser.getUsername()));
                    } else if (message.getContent().split(">")[0].equals("#react")) {
                        int index = Integer.parseInt(message.getContent().split(">")[1]) - 1;
                        String reactionType = message.getContent().split(">")[2].toLowerCase();
                        String reactor = message.getAuthorName();
                        directChatController.reactToMessage(index, reactionType, reactor);
                        directChatController.broadcastExitMessage("reacted to message with reaction of " + reactionType, connections.get(currentUser.getUsername()));
                    } else if (message.getContent().split(">")[0].equals("#music")) {
                        directChatController.broadcastMessage(message);
                    } else if (message.getContent().startsWith("#pause")) {
                        directChatController.broadcastExitMessage("#pause", connections.get(currentUser.getUsername()));
                    } else {
                        directChatController.addMessage(message);
                    }

                } else {
                    inChat = false;
                }
            }
            if (directChatController.numOfUsersInChat() == 0) {
                directChat.stop();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * handles group chat in a text channel.
     */
    private void getTextChannel() {
        try {
            String owner = inputStream.readUTF();
            String guildName = inputStream.readUTF();
            Guild guild = getGuild(owner, guildName);
            String textChannelName = inputStream.readUTF();
            ArrayList<TextChannel> textChannels = guild.getTextChannels();
            TextChannel textChannel = null;
            for (TextChannel t : textChannels) {
                if (t.getName().equals(textChannelName)) {
                    textChannel = t;
                    break;
                }
            }
            if (textChannel != null) {
                outputStream.writeUTF("success.");
                outputStream.flush();
                textChannel.addUser(connections.get(appUsername));
                Message message = null;
                boolean inChat = true;
                while (inChat) {
                    Object obj = inputStream.readObject();
                    if (obj instanceof Message) {
                        message = (Message) obj;
                        String msgContent = message.getContent();
                        if (message.getContent().equals("#exit")) {
                            textChannel.broadcastExitMessage("you exited text channel.", connections.get(appUsername));
                            textChannel.removeUser(connections.get(appUsername));
                            break;
                        } else if (message.getContent().split(">")[0].equals("#pin")) {
                            int index = Integer.parseInt(message.getContent().split(">")[1]);
                            textChannel.pinMessage(index);
                            textChannel.broadcastExitMessage("message pinned successfully.", connections.get(appUsername));
                        } else if (message.getContent().equals("#pins")) {
                            textChannel.showPinnedMessages(connections.get(appUsername));
                        } else if (message.getContent().split(">")[0].equals("#react")) {
                            int index = Integer.parseInt(message.getContent().split(">")[1]) - 1;
                            String reactionType = message.getContent().split(">")[2].toLowerCase();
                            String reactor = message.getAuthorName();
                            textChannel.reactToMessage(index, reactionType, reactor);
                            textChannel.broadcastExitMessage("reacted to message with reaction of " + reactionType, connections.get(appUsername));
                        } else if (msgContent.split(">")[0].equals("#music")) {
                            textChannel.broadcastMessage(message);
                        } else if (message.getContent().startsWith("#pause")) {
                            textChannel.broadcastExitMessage("#pause", connections.get(appUsername));
                        } else {
                            textChannel.addMessage(message);
                        }

                    } else {
                        inChat = false;
                    }
                }
            } else {
                outputStream.writeUTF("failed.");
                outputStream.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * removes a user from direct chat whenever user input is #exit
     */
    private void removeFromDirectChat() {
        try {
            String username = inputStream.readUTF();
            String friendName = inputStream.readUTF();
            String chatHash = directChatHashCode(username, friendName);
            DirectChatController directChat = directChats.get(chatHash);
            directChat.removeConnection(username);
            if (directChat.numOfUsersInChat() == 0) {
                directChats.remove(directChat.getChatHashCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * blocks a user
     */
    private void blockUser() {
        try {
            String user = inputStream.readUTF();
            String blockTarget = inputStream.readUTF();
            String respone = Database.blockUser(user, blockTarget);
            outputStream.writeUTF(respone);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * unblocks a user
     */
    private void unblockUser() {
        try {
            String user = inputStream.readUTF();
            String unblockTarget = inputStream.readUTF();
            String respone = Database.unblockUser(user, unblockTarget);
            outputStream.writeUTF(respone);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends block list of a user
     */
    private void blockList() {
        try {
            String username = inputStream.readUTF();
            HashSet<User> blockedList = Database.viewBlockedList(username);
            outputStream.writeObject(blockedList);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save guilds.
     */
    public void saveGuilds() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("assets/guilds/all_guilds.bin"))) {
            try {
                os.writeObject(allGuilds);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Load guilds.
     */
    public void loadGuilds() {
        //      System.out.println("load:");
        try {
            File theFile = new File("assets/guilds/all_guilds.bin");
            if (!theFile.exists()) {
                theFile.createNewFile();
            }
            ObjectInputStream is = new ObjectInputStream(new FileInputStream("assets/guilds/all_guilds.bin"));
            allGuilds = (HashMap<String, ArrayList<Guild>>) is.readObject();
//            for (Map.Entry<String, ArrayList<Guild>> set :
//                    allGuilds.entrySet()) {
//                System.out.println(set.getKey() + " = "
//                        + set.getValue().isEmpty());
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            if (e instanceof EOFException) {
                System.out.println("all_guilds.bin is empty");
            } else {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add guild.
     */
    public void addGuild() {
        loadGuilds();
        try {
            Guild guild = (Guild) inputStream.readObject();
            String name = guild.getOwnerName();
            ArrayList<Guild> guilds = allGuilds.get(name);
            if (guilds == null) {
                guilds = new ArrayList<>();
            }
            guilds.add(guild);
            allGuilds.put(name, guilds);
            saveGuilds();
            outputStream.writeUTF("Success");
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets guild.
     *
     * @param owner     the owner
     * @param guildName the guild name
     * @return the guild
     */
    public Guild getGuild(String owner, String guildName) {
        ArrayList<Guild> ownerGuilds = allGuilds.get(owner);
        for (Guild g : ownerGuilds) {
            if (g.getName().equals(guildName)) {
                return g;
            }
        }
        return null;
    }

    /**
     * Gets guild.
     *
     * @return the guild
     */
    public Guild getGuild() {
        Guild guild = null;
        try {
            //String owner = inputStream.readUTF();
            String guildName = inputStream.readUTF();
            Collection<ArrayList<Guild>> allGuild = allGuilds.values();
            for (ArrayList<Guild> guilds : allGuild) {
                for (Guild g : guilds) {
                    if (g.getName().equals(guildName)) {
                        guild = g;
                        break;
                    }
                }
            }
            if (guild != null) {
                for (GuildUser guildUser : guild.getGuildUsers()) {
                    guildUser.setStatus(Database.retrieveFromDB(guildUser.getUsername()).getStatus());
                }
            }
            outputStream.reset();
            outputStream.writeUnshared(guild);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return guild;
    }

    /**
     * adds a member to server
     */
    private void addMemberToServer() {
        try {
            GuildUser gUser = (GuildUser) inputStream.readObject();
            String gOwner = inputStream.readUTF();
            String guildName = inputStream.readUTF();
            Guild guild = getGuild(gOwner, guildName);
            guild.addUser(gUser);
            saveGuilds();
            outputStream.writeUTF(gUser.getUsername() + " added to server successfully");
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * adds a text channel to server
     */
    private void addTextChannel() {
        try {
            String textChannelName = inputStream.readUTF();
            String guildOwnerName = inputStream.readUTF();
            String guildName = inputStream.readUTF();
            Guild guild = getGuild(guildOwnerName, guildName);
            boolean exists = false;
            for (TextChannel textChannel : guild.getTextChannels()) {
                if (textChannel.getName().equals(textChannelName)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                outputStream.writeUTF("this text channel already exists.");
                outputStream.flush();
                return;
            }
            //GroupChat groupChat = new GroupChat();groupChat
            TextChannel textChannel = new TextChannel(textChannelName, guildName);
            guild.addTextChanel(textChannel);
            saveGuilds();
            outputStream.writeUTF("text channel added successfully.");
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteMemberToServer() {
        try {
            GuildUser gUser = (GuildUser) inputStream.readObject();
            String gOwner = inputStream.readUTF();
            String guildName = inputStream.readUTF();
            Guild guild = getGuild(gOwner, guildName);
            guild.removeMember(gUser.getUsername());
            saveGuilds();
            outputStream.writeUTF(gUser.getUsername() + " removed from server successfully");
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Change guild name.
     */
    public void changeGuildName() {
        try {
            String gOwner = inputStream.readUTF();
            String guildName = inputStream.readUTF();
            String guildNewName = inputStream.readUTF();
            Guild renamedGuild = getGuild(gOwner, guildName);
            allGuilds.get(gOwner).remove(renamedGuild);
            renamedGuild.setName(guildNewName);
            allGuilds.get(gOwner).add(renamedGuild);
            saveGuilds();
            outputStream.writeUTF("guild name change from " + guildName + " to " + guildNewName);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * List of user servers.
     */
    public void listOfUserServers() {
        ArrayList<Guild> userGuilds = new ArrayList<>();
        try {
            if (allGuilds.values().isEmpty()) {
                loadGuilds();
            }
            String username = inputStream.readUTF();
            for (ArrayList<Guild> guilds : allGuilds.values()) {
                for (Guild guild : guilds) {
                    if (guild.hasUser(username)) {
                        userGuilds.add(guild);
                    }
                }
            }
            outputStream.writeUnshared(userGuilds);
            outputStream.flush();
            outputStream.reset();
            userGuilds.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (!socket.isClosed()) {
            getService();
        }
    }
}


package com.discordapp.Database;

import com.discordapp.controller.ServerErrorType;
import com.discordapp.Model.Status;
import com.discordapp.Model.User;

import java.sql.*;
import java.util.HashSet;

/**
 * The type com.discordapp.Database.
 */
public class Database {
    /**
     * Connect to db.
     *
     * @return the connection
     */
    public static Connection connectToDB() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/discord", "root", "177013");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Insert a user into db.
     *
     * @param username    the username
     * @param password    the password
     * @param email       the email
     * @param phoneNumber the phone number
     * @param token       the token
     * @param avatar      the avatar
     * @return the server error type
     */
    public static ServerErrorType insertToDB(String username, String password, String email, String phoneNumber, String token, byte[] avatar) {

        try {
            Connection connection = connectToDB();
            if (connection == null) {
                return ServerErrorType.SERVER_CONNECTION_FAILED;
            }
            Statement checkuserExists = connection.createStatement();
            ResultSet foundUser = checkuserExists.executeQuery("select * from users where userName = " + "'" + username + "'");
            if (foundUser.next()) {
                return ServerErrorType.USER_ALREADY_EXISTS;
            }
            foundUser = checkuserExists.executeQuery("select * from users where email = " + "'" + email + "'");
            if (foundUser.next()) {
                return ServerErrorType.USER_ALREADY_EXISTS;
            }
            String insertQuery = "insert into users (userName, password, email, phoneNumber,userID,avatar,status) values(?, ?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);
            statement.setString(5, token);
            statement.setBytes(6, avatar);
            statement.setString(7,"offline");
            statement.execute();
            connection.close();
            return ServerErrorType.NO_ERROR;
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerErrorType.DATABASE_ERROR;
        }

    }


    /**
     * Retrieve user from db .
     *
     * @param username the username
     * @param password the password
     * @return the user
     */
    public static User retrieveFromDB(String username, String password) {
        try {
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where userName = " + "'" + username + "'");
            if (!resultSet.next()) {
                connection.close();
                return null;
            } else {
                String realPassword = resultSet.getString("password");
                if (password.equals(realPassword)) {
                    User user = createUser(resultSet);
                    connection.close();
                    return user;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieve user from db with only username.
     *
     * @param username the username
     * @return the user
     */
    public static User retrieveFromDB(String username) {
        try {

            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where userName = " + "'" + username + "'");
            if (!resultSet.next()) {
                return null;
            }
            User user = createUser(resultSet);
            connection.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Create user.
     *
     * @param resultSet the result set
     * @return the user
     * @throws SQLException the sql exception
     */
    public static User createUser(ResultSet resultSet) throws SQLException {
        String username = resultSet.getString("userName");
        String password = resultSet.getString("password");
        String email = resultSet.getString("email");
        String phoneNumber = resultSet.getString("phoneNumber");
        String uId = resultSet.getString("userID");
        byte[] avatar = null;
        avatar = resultSet.getBytes("avatar");
        String strStatus = resultSet.getString("status");
        Status status = null;
        switch (strStatus) {
            case "online" -> {
                status = Status.ONLINE;
            }
            case "offline" -> {
                status = Status.INVISIBLE;
            }
            case "idle" -> {
                status = Status.IDLE;
            }
            case "do not disturb" -> {
                status = Status.DO_NOT_DISTURB;
            }
            default -> status = Status.INVISIBLE;
        }
        User user = new User(username, password, email, phoneNumber, uId, avatar, status);
        return user;

    }

    /**
     * Send friend request.
     *
     * @param fromUser   the from user
     * @param targetUser the target user
     * @return the server error type
     */
    public static ServerErrorType sendFriendRequest(String fromUser, String targetUser) {
        try {
            Connection connection = connectToDB();
            {
                Statement checkIfAlreadyExists = connection.createStatement();
                ResultSet checkRules = checkIfAlreadyExists.executeQuery("select from_user, to_user from requests where from_user = " + "'" + fromUser + "'" + "and to_user = " + "'" + targetUser + "'");
                boolean firstRule = false;
                boolean secondRule = false;
                if (!checkRules.next()) {
                    firstRule = true;
                }
                checkRules = checkIfAlreadyExists.executeQuery("select from_user, to_user from requests where from_user = " + "'" + targetUser + "'" + "and to_user = " + "'" + fromUser + "'");
                if (!checkRules.next()) {
                    secondRule = true;
                }
                if (!(firstRule & secondRule)) {
                    return ServerErrorType.Duplicate_ERROR;
                }
                Statement CheckIfAlreadyFriend = connection.createStatement();
                ResultSet isFriend = checkIfAlreadyExists.executeQuery("select user_id from friends where friends_id = " + "'" + fromUser + "'" + "and user_id = " + "'" + targetUser + "'");
                if (isFriend.next()) {
                    return ServerErrorType.ALREADY_FRIEND;
                }
                Statement checkIfUserExists = connection.createStatement();
                ResultSet doesExist = checkIfAlreadyExists.executeQuery("select userName from users where userName = " +  "'" + targetUser + "'");
                if(!doesExist.next()){
                    return ServerErrorType.Duplicate_ERROR;
                }
            }
            String insertQuery = "insert into requests (to_user, from_user) values(?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, targetUser);
            statement.setString(2, fromUser);
            statement.execute();
            connection.close();
            return ServerErrorType.NO_ERROR;
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            return ServerErrorType.Duplicate_ERROR;
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerErrorType.DATABASE_ERROR;
        }
    }

    /**
     * retrieve friend request list.
     *
     * @param targetUser the target user
     * @return friend request list
     */
    public static HashSet<User> viewFriendRequestList(String targetUser) {
        HashSet<User> reqList = new HashSet<>();
        try {
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select from_user from requests where to_user = " + "'" + targetUser + "'");
            while (resultSet.next()) {
                reqList.add(retrieveFromDB(resultSet.getString(1)));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reqList;
    }

    /**
     * retrieve friend list from db.
     *
     * @param username the username
     * @return friend list
     */
    public static HashSet<User> viewFriendList(String username) {
        HashSet<User> friendList = new HashSet<>();
        try {
            Connection connection = connectToDB();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select friends_id from friends where user_id = " + "'" + username + "'");
            while (resultSet.next()) {
                friendList.add(retrieveFromDB(resultSet.getString(1)));
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendList;
    }

    /**
     * updates friend requests list.
     *
     * @param username the username
     * @param accepted the accepted in list
     * @param rejected the rejected in list
     * @return query result
     */
    public static String reviseFriendRequests(String username, HashSet<String> accepted, HashSet<String> rejected) {
        try {
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            PreparedStatement addtoFriends = connection.prepareStatement("INSERT into friends (user_id,friends_id) values(?,?) ");
            for (String friendUsername : accepted) {
                addtoFriends.setString(1, username);
                addtoFriends.setString(2, friendUsername);
                addtoFriends.execute();
                addtoFriends.setString(1, friendUsername);
                addtoFriends.setString(2, username);
                addtoFriends.execute();
                statement.execute("DELETE from requests where from_user = " + "'" + friendUsername + "'" + " and to_user = " + "'" + username + "'");
            }
            for (String friendUsername : rejected) {
                statement.execute("DELETE from requests where from_user = " + "'" + friendUsername + "'" + " and to_user = " + "'" + username + "'");
            }

            connection.close();
            return "Requests List updated.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "something went wrong.";
        }
    }

    /**
     * Block user.
     *
     * @param user        the user
     * @param blockTarget the block target
     * @return result of query
     */
    public static String blockUser(String user, String blockTarget) {
        try {
            Connection connection = connectToDB();
            Statement checkIfAlreadyBlocked = connection.createStatement();
            ResultSet checkRules = checkIfAlreadyBlocked.executeQuery("select blocker, blocked_person from blocked_users" +
                    " where blocker = " + "'" + user + "'" + "and blocked_person = " + "'" + blockTarget + "'");
            if (!checkRules.next()) {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into blocked_users (blocker,blocked_person) values(?,?)");
                preparedStatement.setString(1, user);
                preparedStatement.setString(2, blockTarget);
                preparedStatement.execute();
                Statement statement = connection.createStatement();
                statement.execute("DELETE from friends" + " where user_id = " + "'" + user + "'" + "and friends_id = " + "'" + blockTarget + "'");
                connection.close();
                return "user successfully blocked.";
            } else {
                connection.close();
                return "user is already blocked.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "couldn't block user.";
        }

    }

    /**
     * Unblock user in db.
     *
     * @param user          the user
     * @param unblockTarget the unblock target
     * @return result of query
     */
    public static String unblockUser(String user, String unblockTarget) {
        try {
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select blocked_person from blocked_users where  blocker = " + "'" + user + "'" + "and blocked_person = " + "'" + unblockTarget + "'");
            if (!resultSet.next()) {
                return "this person isn't blocked.";
            }
            statement.execute("delete from blocked_users where blocker = " + "'" + user + "'" + "and blocked_person = " + "'" + unblockTarget + "'");
            connection.close();
            return "successfully unblocked user.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "something went wrong while unblocking user.";
        }

    }

    /**
     * retrieve block list from db.
     *
     * @param username the username
     * @return the block list hash set
     */
    public static HashSet<User> viewBlockedList(String username) {
        HashSet<User> blockedList = new HashSet<>();
        try {
            Connection connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select blocked_person from blocked_users where blocker = " + "'" + username + "'");

            while (resultSet.next()) {
                blockedList.add(Database.retrieveFromDB(resultSet.getString(1)));
            }
            connection.close();
            return blockedList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blockedList;

    }

    /**
     * Update user in db.
     *
     * @param user the user
     * @return the boolean
     */
    public static boolean updateUser(User user) {
        Connection connection = connectToDB();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Users SET password=?, status=?, avatar=? WHERE userName =?");
            Status userStatus = user.getStatus();
            String status = userStatus.toString(userStatus);
            byte[] byteArr = user.getAvatar();
            statement.setString(1, user.getPassword());
            statement.setString(2, status);
            statement.setBytes(3, byteArr);
            statement.setString(4, user.getUsername());
            statement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}




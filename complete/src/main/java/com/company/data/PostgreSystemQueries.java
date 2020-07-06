package com.company.data;

import com.company.data.model.Email;
import com.company.data.model.Search;
import com.company.data.model.User;
import com.company.data.model.Watchlist;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;

public class PostgreSystemQueries {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "mwlsuper";
    private static final String password = "myWatchlistSuper=Super";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static int insertUser(User user) {

        String name = user.getName();
        String password = user.getPassword();
        String emailAddress = user.getEmailAddress();
        Boolean isValidated = user.getValidated();
        Boolean isRegistered = user.getRegistered();
        int validationAttempts = user.getValidationAttempts();
        String token = user.getToken();
        Instant tokenExpiryDate = user.getTokenExpiryDate();

        Timestamp timestamp = new Timestamp(tokenExpiryDate.toEpochMilli());

        String sql = "INSERT INTO users (name, password, emailaddress, isvalidated, isregistered, validationattempts, token, tokenExpiryDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        int rowCount = 0;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, emailAddress);
            stmt.setBoolean(4, isValidated);
            stmt.setBoolean(5, isRegistered);
            stmt.setInt(6, validationAttempts);
            stmt.setString(7, token);
            stmt.setTimestamp(8, timestamp);

            rowCount = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int updateUser(User user) {

        long id = user.getId();
        String name = user.getName();
        String password = user.getPassword();
        String emailAddress = user.getEmailAddress();
        Boolean isValidated = user.getValidated();
        Boolean isRegistered = user.getRegistered();
        int validationAttempts = user.getValidationAttempts();
        String token = user.getToken();
        Instant tokenExpiryDate = user.getTokenExpiryDate();

        Timestamp timestamp = new Timestamp(tokenExpiryDate.toEpochMilli());

        String sql = "UPDATE users SET name = ?, password = ?, emailaddress = ?, isvalidated = ?, isregistered = ?, validationattempts = ?, token = ?, tokenexpirydate = ? WHERE id = ?";
        int rowCount = 0;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, emailAddress);
            stmt.setBoolean(4, isValidated);
            stmt.setBoolean(5, isRegistered);
            stmt.setInt(6, validationAttempts);
            stmt.setString(7, token);
            stmt.setTimestamp(8, timestamp);
            stmt.setLong(9, id);

            rowCount = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int deleteUserById(long userId) {

        String sql = "DELETE FROM users WHERE id = ?";
        int rowCount = 0;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            rowCount = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static ArrayList<User> getUsers() {

        ArrayList<User> newUsers = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String emailAddress = rs.getString("emailaddress");
                Boolean isValidated = rs.getBoolean("isvalidated");
                Boolean isRegistered = rs.getBoolean("isregistered");
                int validationAttempts = rs.getInt("validationattempts");
                String token = rs.getString("token");
                Timestamp timestamp = rs.getTimestamp("tokenExpiryDate");

                Instant tokenExpiryDate = Instant.ofEpochMilli(timestamp.getTime());

                User user = new User(id, name, password, emailAddress, isValidated, isRegistered, validationAttempts, token, tokenExpiryDate);
                newUsers.add(user);
            }

            User.setUsers(newUsers);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return User.getUsers();
    }

    public static int getUserCount() {

        String sql = "SELECT count(*) FROM users";
        int userCount = 0;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            rs.next();
            userCount = rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userCount;
    }

    public static User getUserById(long userId) {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String emailAddress = rs.getString("emailaddress");
                Boolean isValidated = rs.getBoolean("isvalidated");
                Boolean isRegistered = rs.getBoolean("isregistered");
                int validationAttempts = rs.getInt("validationattempts");
                String token = rs.getString("token");
                Timestamp timestamp = rs.getTimestamp("tokenExpiryDate");

                Instant tokenExpiryDate = Instant.ofEpochMilli(timestamp.getTime());

                return new User(id, name, password, emailAddress, isValidated, isRegistered, validationAttempts, token, tokenExpiryDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static User getUserByName(String userName) {

        String sql = "SELECT * FROM users WHERE name = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String emailAddress = rs.getString("emailaddress");
                Boolean isValidated = rs.getBoolean("isvalidated");
                Boolean isRegistered = rs.getBoolean("isregistered");
                int validationAttempts = rs.getInt("validationattempts");
                String token = rs.getString("token");
                Timestamp timestamp = rs.getTimestamp("tokenExpiryDate");

                Instant tokenExpiryDate = Instant.ofEpochMilli(timestamp.getTime());

                return new User(id, name, password, emailAddress, isValidated, isRegistered, validationAttempts, token, tokenExpiryDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int insertEmail(Email email, User user) {

        String type = email.getType();
        String subject = email.getSubject();
        String body = email.getBody();

        String sql1 = "INSERT INTO email (type, subject, body) VALUES(?, ?, ?)";
        int rowCount = 0;

        try (Connection conn1 = connect();
             PreparedStatement stmt1 = conn1.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {

            stmt1.setString(1, type);
            stmt1.setString(2, subject);
            stmt1.setString(3, body);

            rowCount = stmt1.executeUpdate();
            ResultSet keys = stmt1.getGeneratedKeys();
            keys.next();

            long emailId = keys.getLong(1);
            long userId = user.getId();

            String sql2 = "INSERT INTO email_users (email_id, user_id) VALUES(?, ?)";

            try (Connection conn2 = connect();
                 PreparedStatement stmt2 = conn2.prepareStatement(sql2)) {

                stmt2.setLong(1, emailId);
                stmt2.setLong(2, userId);

                stmt2.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int deleteEmailById(long emailId) {

        String sql = "DELETE FROM email WHERE id = ?";
        int rowCount = 0;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, emailId);
            rowCount = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int getEmailCount() {

        String sql = "SELECT count(*) FROM email";
        int emailCount = 0;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            rs.next();
            emailCount = rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailCount;
    }

    public static int insertList(Watchlist watchlist, User user) {

        String title = watchlist.getTitle();
        String description = watchlist.getDescription();
        ArrayList listItems = watchlist.getListItems();

        JSONArray obj = new JSONArray();
        if (listItems != null) {
            obj.addAll(listItems);
        }

        String sql1 = "INSERT INTO watchlist (title, description, listitems) VALUES(?, ?, ?)";
        int rowCount = 0;

        try (Connection conn1 = connect();
             PreparedStatement stmt1 = conn1.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {

            stmt1.setString(1, title);
            stmt1.setString(2, description);
            stmt1.setObject(3, obj.toJSONString());

            rowCount = stmt1.executeUpdate();
            ResultSet keys = stmt1.getGeneratedKeys();
            keys.next();

            long listId = keys.getLong(1);
            long userId = user.getId();

            String sql2 = "INSERT INTO watchlist_users (watchlist_id, user_id) VALUES(?, ?)";

            try (PreparedStatement stmt2 = conn1.prepareStatement(sql2)) {

                stmt2.setLong(1, listId);
                stmt2.setLong(2, userId);

                stmt2.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int updateList(Watchlist watchlist) {

        long id = watchlist.getId();
        String title = watchlist.getTitle();
        String description = watchlist.getDescription();
        ArrayList listItems = watchlist.getListItems();

        JSONArray obj = new JSONArray();
        obj.addAll(listItems);

        String sql = "UPDATE watchlist SET title = ?, description = ?, listitems = ? WHERE id = ?";
        int rowCount = 0;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setObject(3, obj.toJSONString());
            stmt.setLong(4, id);

            rowCount = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int deleteList(Watchlist watchlist) {

        String sql = "DELETE FROM watchlist WHERE id = ?";
        int rowCount = 0;

        long id = watchlist.getId();

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            rowCount = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static ArrayList<Watchlist> getAllListsByUser(User user) {

        ArrayList<Watchlist> newLists = new ArrayList<>();
        String sql = "select wu.watchlist_id, w.title, w.description, w.listItems\n" +
                "from watchlist_users wu\n" +
                "join users u on wu.user_id = u.id\n" +
                "join watchlist w on wu.watchlist_id = w.id\n" +
                "where u.name = ?\n" +
                "order by wu.watchlist_id";

        String userName = user.getName();

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userName);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("watchlist_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                Object object = rs.getObject("listItems");

                JSONArray array = (JSONArray) JSONValue.parse(object.toString());
                ArrayList<Object> listItems = new ArrayList<>();

                if (array != null) {
                    listItems.addAll(array);
                }

                Watchlist watchlist = new Watchlist(id, title, description, listItems);
                newLists.add(watchlist);
            }

            Watchlist.setWatchlists(newLists);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Watchlist.getWatchlists();
    }

    public static Watchlist getList(String listTitle, String userName) {

        String sql = "select wu.watchlist_id, w.title, w.description, w.listItems\n" +
                "from watchlist_users wu\n" +
                "join users u on wu.user_id = u.id\n" +
                "join watchlist w on wu.watchlist_id = w.id\n" +
                "where w.title =? AND u.name = ?\n" +
                "order by wu.watchlist_id";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, listTitle);
            stmt.setString(2, userName);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("watchlist_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                Object object = rs.getObject("listItems");

                JSONArray array = (JSONArray) JSONValue.parse(object.toString());
                ArrayList<Object> listItems = new ArrayList<>();

                if (array != null) {
                    listItems.addAll(array);
                }

                return new Watchlist(id, title, description, listItems);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int insertSearch(Search search) {

        String title = search.getTitle();
        String year = search.getYear();
        int returnValue = search.getReturnValue();

        String sql1 = "INSERT INTO moviesearch (title, year, returnvalue) VALUES(?, ?, ?)";
        int rowCount = 0;

        try (Connection conn1 = connect();
             PreparedStatement stmt1 = conn1.prepareStatement(sql1)) {

            stmt1.setString(1, title);
            stmt1.setString(2, year);
            stmt1.setInt(3, returnValue);

            rowCount = stmt1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        String sql2 = "INSERT INTO moviesearch_users (search_id, user_id) VALUES(?, ?)";
//
//        try (Connection conn = connect();
//             PreparedStatement stmt = conn.prepareStatement(sql2)) {
//
//            stmt.setLong(1, id);
//            stmt.setLong(2, userId);
//
//            stmt.executeUpdate();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return rowCount;
    }

    public static int updateSearch(Search search) {

        long id = search.getId();
        String title = search.getTitle();
        String year = search.getYear();
        int returnValue = search.getReturnValue();

        String sql1 = "UPDATE moviesearch SET title = ?, year = ?, returnvalue = ? WHERE id = ?";
        int rowCount = 0;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql1)) {

            stmt.setString(1, title);
            stmt.setString(2, year);
            stmt.setInt(3, returnValue);
            stmt.setLong(4, id);

            rowCount = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }
}
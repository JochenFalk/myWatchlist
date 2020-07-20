package com.company.data;

import com.company.data.model.Email;
import com.company.data.model.Search;
import com.company.data.model.User;
import com.company.data.model.Watchlist;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

public class PostgreSystemQueries {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "mwlsuper";
    private static final String password = "myWatchlistSuper=Super";
    private static boolean next;

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
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, emailAddress);
            stmt.setBoolean(4, isValidated);
            stmt.setBoolean(5, isRegistered);
            stmt.setInt(6, validationAttempts);
            stmt.setString(7, token);
            stmt.setTimestamp(8, timestamp);

            rowCount = stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            keys.next();

            long userId = keys.getLong(1);
            String role = "User";

            String sql2 = "INSERT INTO roles (user_id, role) VALUES(?, ?)";

            try (Connection conn2 = connect();
                 PreparedStatement stmt2 = conn2.prepareStatement(sql2)) {

                stmt2.setLong(1, userId);
                stmt2.setString(2, role);

                stmt2.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int insertAdmin(User user) {

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
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, emailAddress);
            stmt.setBoolean(4, isValidated);
            stmt.setBoolean(5, isRegistered);
            stmt.setInt(6, validationAttempts);
            stmt.setString(7, token);
            stmt.setTimestamp(8, timestamp);

            rowCount = stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            keys.next();

            long userId = keys.getLong(1);
            String role = "Admin";

            String sql2 = "INSERT INTO roles (user_id, role) VALUES(?, ?)";

            try (Connection conn2 = connect();
                 PreparedStatement stmt2 = conn2.prepareStatement(sql2)) {

                stmt2.setLong(1, userId);
                stmt2.setString(2, role);

                stmt2.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int updateUser(User user) {

        int rowCount = 0;

        long id = user.getId();
        String name = user.getName();
        String role = user.getRole();
        String password = user.getPassword();
        String emailAddress = user.getEmailAddress();
        Boolean isValidated = user.getValidated();
        Boolean isRegistered = user.getRegistered();
        int validationAttempts = user.getValidationAttempts();
        String token = user.getToken();
        Instant tokenExpiryDate = user.getTokenExpiryDate();

        Timestamp timestamp = new Timestamp(tokenExpiryDate.toEpochMilli());

        try (Connection conn = connect()) {

            String sql1 = "UPDATE users SET name = ?, password = ?, emailaddress = ?, isvalidated = ?, isregistered = ?, validationattempts = ?, token = ?, tokenexpirydate = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
                stmt.setString(1, name);
                stmt.setString(2, password);
                stmt.setString(3, emailAddress);
                stmt.setBoolean(4, isValidated);
                stmt.setBoolean(5, isRegistered);
                stmt.setInt(6, validationAttempts);
                stmt.setString(7, token);
                stmt.setTimestamp(8, timestamp);
                stmt.setLong(9, id);
                stmt.executeUpdate();
            }

            String sql2 = "UPDATE roles SET role = ? WHERE  user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                stmt.setString(1, role);
                stmt.setLong(2, id);
                rowCount = stmt.executeUpdate();
            }

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

    public static ArrayList<User> getAllUsers() {

        ArrayList<User> newUsers = new ArrayList<>();

        String sql = "SELECT * \n" +
                "FROM users u\n" +
                "JOIN roles r on u.id = r.user_id\n" +
                "order by id";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String role = rs.getString("role");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String emailAddress = rs.getString("emailaddress");
                Boolean isValidated = rs.getBoolean("isvalidated");
                Boolean isRegistered = rs.getBoolean("isregistered");
                int validationAttempts = rs.getInt("validationattempts");
                String token = rs.getString("token");
                Timestamp timestamp = rs.getTimestamp("tokenExpiryDate");

                Instant tokenExpiryDate = Instant.ofEpochMilli(timestamp.getTime());

                User user = new User(id, role, name, password, emailAddress, isValidated, isRegistered, validationAttempts, token, tokenExpiryDate);
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

        String sql = "SELECT * \n" +
                "FROM users u\n" +
                "JOIN roles r on u.id = r.user_id\n" +
                "WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("id");
                String role = rs.getString("role");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String emailAddress = rs.getString("emailaddress");
                Boolean isValidated = rs.getBoolean("isvalidated");
                Boolean isRegistered = rs.getBoolean("isregistered");
                int validationAttempts = rs.getInt("validationattempts");
                String token = rs.getString("token");
                Timestamp timestamp = rs.getTimestamp("tokenExpiryDate");

                Instant tokenExpiryDate = Instant.ofEpochMilli(timestamp.getTime());

                return new User(id, role, name, password, emailAddress, isValidated, isRegistered, validationAttempts, token, tokenExpiryDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static User getUserByName(String userName) {

        String sql = "SELECT * \n" +
                "FROM users u\n" +
                "JOIN roles r on u.id = r.user_id\n" +
                "WHERE name = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("id");
                String role = rs.getString("role");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String emailAddress = rs.getString("emailaddress");
                Boolean isValidated = rs.getBoolean("isvalidated");
                Boolean isRegistered = rs.getBoolean("isregistered");
                int validationAttempts = rs.getInt("validationattempts");
                String token = rs.getString("token");
                Timestamp timestamp = rs.getTimestamp("tokenExpiryDate");

                Instant tokenExpiryDate = Instant.ofEpochMilli(timestamp.getTime());

                return new User(id, role, name, password, emailAddress, isValidated, isRegistered, validationAttempts, token, tokenExpiryDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static User getUserByEmailId(long emailId) {

        String sql = "SELECT * \n" +
                "FROM email_users eu\n" +
                "JOIN users u on u.id = eu.user_id\n" +
                "JOIN roles r on u.id = r.user_id\n" +
                "WHERE email_id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, emailId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("id");
                String role = rs.getString("role");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String emailAddress = rs.getString("emailaddress");
                Boolean isValidated = rs.getBoolean("isvalidated");
                Boolean isRegistered = rs.getBoolean("isregistered");
                int validationAttempts = rs.getInt("validationattempts");
                String token = rs.getString("token");
                Timestamp timestamp = rs.getTimestamp("tokenExpiryDate");

                Instant tokenExpiryDate = Instant.ofEpochMilli(timestamp.getTime());

                return new User(id, role, name, password, emailAddress, isValidated, isRegistered, validationAttempts, token, tokenExpiryDate);
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

    public static int updateEmail(Email email, User user) {

        int rowCount = 0;

        long emailId = email.getId();
        String type = email.getType();
        String recipient = email.getRecipient();
        String subject = email.getSubject();
        String body = email.getBody();

        long userId = user.getId();

        try (Connection conn = connect()) {

            String sql1 = "UPDATE email SET type = ?, subject = ?, body = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
                stmt.setString(1, type);
                stmt.setString(2, subject);
                stmt.setString(3, body);
                stmt.setLong(4, emailId);
                stmt.executeUpdate();
            }

            String sql2 = "UPDATE users SET emailaddress = ? WHERE  id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                stmt.setString(1, recipient);
                stmt.setLong(2, userId);
                rowCount = stmt.executeUpdate();
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

    public static ArrayList<Email> getAllEmails() {

        ArrayList<Email> newEmails = new ArrayList<>();

        String sql = "select e.id, e.type, e.subject, e.body, u.emailaddress\n" +
                "from email_users eu\n" +
                "join users u on u.id = eu.user_id\n" +
                "join email e on e.id = eu.email_id\n" +
                "order by eu.user_id;";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String type = rs.getString("type");
                String emailAddress = rs.getString("emailaddress");
                String subject = rs.getString("subject");
                String body = rs.getString("body");

                Email email = new Email(id, type, emailAddress, subject, body);
                newEmails.add(email);
            }

            Email.setEmails(newEmails);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Email.getEmails();
    }

    public static int insertList(Watchlist watchlist, User user) {

        int rowCount = 0;
        String title = watchlist.getTitle();
        String description = watchlist.getDescription();
        ArrayList listItems = watchlist.getListItems();

        JSONArray obj = new JSONArray();
        if (listItems != null) {
            obj.addAll(listItems);
        }

        String sql1 = "INSERT INTO watchlist (title, description, listitems) VALUES(?, ?, ?)";

        try (Connection conn1 = connect();
             PreparedStatement stmt1 = conn1.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {

            stmt1.setString(1, title);
            stmt1.setString(2, description);
            stmt1.setObject(3, obj.toJSONString());

            stmt1.executeUpdate();
            ResultSet keys = stmt1.getGeneratedKeys();
            keys.next();

            long listId = keys.getLong(1);
            long userId = user.getId();

            String sql2 = "INSERT INTO watchlist_users (watchlist_id, user_id) VALUES(?, ?)";

            try (PreparedStatement stmt2 = conn1.prepareStatement(sql2)) {

                stmt2.setLong(1, listId);
                stmt2.setLong(2, userId);

                rowCount = stmt2.executeUpdate();

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

    public static int deleteListById(Long id) {

        int rowCount = 0;

        String sql = "DELETE FROM watchlist WHERE id = ?";

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

    public static ArrayList<Watchlist> getAllLists() {

        ArrayList<Watchlist> newLists = new ArrayList<>();
        String sql = "select wu.watchlist_id, w.title, w.description, w.listItems\n" +
                "from watchlist_users wu\n" +
                "join users u on wu.user_id = u.id\n" +
                "join watchlist w on wu.watchlist_id = w.id\n" +
                "order by wu.watchlist_id";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    public static Watchlist getListByTitleAndUser(String listTitle, String userName) {

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

    public static int insertSearch(Search search, User user) {

        int rowCount = 0;
        String title = search.getTitle();
        String year = search.getYear();
        byte[] poster = search.getPoster();
        int returnValue = search.getReturnValue();
        String creationDate = search.getCreationDate().toString();
        long searchId = 0;
        long userId = 0;

        HashMap<String, String> resultMap = search.getResults();
        JSONObject result = new JSONObject(resultMap);

        ArrayList<Object> cast = search.getCast();
        JSONArray castArray = new JSONArray();
        if (cast != null) {
            castArray.addAll(cast);
        }

        ArrayList<Object> crew = search.getCrew();
        JSONArray crewArray = new JSONArray();
        if (crew != null) {
            crewArray.addAll(crew);
        }

        try (Connection conn = connect()) {
            String sql1 = "INSERT INTO moviesearch (title, year, poster, returnvalue, creationdate) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, title);
                stmt.setString(2, year);
                stmt.setBytes(3, poster);
                stmt.setInt(4, returnValue);
                stmt.setString(5, creationDate);
                stmt.executeUpdate();

                ResultSet keys = stmt.getGeneratedKeys();
                keys.next();

                searchId = keys.getLong(1);
                userId = user.getId();
            }

            String sql2 = "INSERT INTO searchresults (value, search_id) VALUES(?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                stmt.setObject(1, result.toJSONString());
                stmt.setLong(2, searchId);
                stmt.executeUpdate();
            }

            String sql3 = "INSERT INTO moviecast (value, search_id) VALUES(?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
                stmt.setObject(1, castArray.toJSONString());
                stmt.setLong(2, searchId);
                stmt.executeUpdate();
            }

            String sql4 = "INSERT INTO moviecrew (value, search_id) VALUES(?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql4)) {
                stmt.setObject(1, crewArray.toJSONString());
                stmt.setLong(2, searchId);
                stmt.executeUpdate();
            }

            String sql5 = "INSERT INTO moviesearch_users (search_id, user_id) VALUES(?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql5)) {
                stmt.setLong(1, searchId);
                stmt.setLong(2, userId);
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            rowCount++;
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int updateSearch(Search search) {

        long id = search.getId();
        String title = search.getTitle();
        String year = search.getYear();
        byte[] poster = search.getPoster();
        int returnValue = search.getReturnValue();
        String creationDate = search.getCreationDate().toString();

        String sql = "UPDATE moviesearch SET title = ?, year = ?, poster = ?, returnvalue = ?, creationdate = ? WHERE id = ?";
        int rowCount = 0;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, year);
            stmt.setBytes(3, poster);
            stmt.setInt(4, returnValue);
            stmt.setString(5, creationDate);
            stmt.setLong(6, id);

            rowCount = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static Search getSearch(String movieTitle, String movieYear) {

        String sql = "select ms.id, ms.title, ms.poster, ms.year, ms.returnvalue, ms.creationdate, sr.value as results, ct.value as cast, cw.value as crew\n" +
                "from moviesearch ms\n" +
                "join searchresults sr on sr.search_id = ms.id\n" +
                "join moviecast ct on ct.search_id = ms.id\n" +
                "join moviecrew cw on cw.search_id = ms.id\n" +
                "where upper(ms.title) = upper(?) and ms.year =?\n" +
                "order by ms.id;";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, movieTitle);
            stmt.setString(2, movieYear);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String year = rs.getString("year");
                byte[] poster = rs.getBytes("poster");
                int returnValue = rs.getInt("returnvalue");
                String creationDateString = rs.getString("creationdate");
                Object resultObject = rs.getObject("results");
                Object crewObject = rs.getObject("crew");
                Object castObject = rs.getObject("cast");

                Instant creationDate = Instant.parse(creationDateString);

                HashMap<String, String> resultMap = new HashMap<>();
                JSONObject object = (JSONObject) JSONValue.parse(resultObject.toString());

                for (Object keyString : object.keySet()) {
                    Object keyValue = object.get(keyString);
                    if (object.get(keyString) != null) {
                        resultMap.put(keyString.toString(), keyValue.toString());
                    }
                }

                JSONArray crewArray = (JSONArray) JSONValue.parse(crewObject.toString());
                ArrayList<Object> crew = new ArrayList<>();
                if (crewArray != null) {
                    crew.addAll(crewArray);
                }
                JSONArray castArray = (JSONArray) JSONValue.parse(castObject.toString());
                ArrayList<Object> cast = new ArrayList<>();
                if (castArray != null) {
                    cast.addAll(castArray);
                }

                Search newSearch = new Search(title, year, returnValue, creationDate);
                newSearch.setId(id);
                newSearch.setResults(resultMap);
                newSearch.setCrew(crew);
                newSearch.setCast(cast);
                newSearch.setPoster(poster);

                return newSearch;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Search> getAllSearches() {

        ArrayList<Search> newSearches = new ArrayList<>();

        String sql = "select ms.id, ms.title, ms.year, ms.poster, ms.returnvalue, ms.creationdate, sr.value as results, ct.value as cast, cw.value as crew\n" +
                "from moviesearch ms\n" +
                "join searchresults sr on sr.search_id = ms.id\n" +
                "join moviecast ct on ct.search_id = ms.id\n" +
                "join moviecrew cw on cw.search_id = ms.id\n" +
                "order by ms.id;";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String year = rs.getString("year");
                byte[] poster = rs.getBytes("poster");
                int returnValue = rs.getInt("returnvalue");
                String creationDateString = rs.getString("creationdate");
                Object resultObject = rs.getObject("results");
                Object crewObject = rs.getObject("crew");
                Object castObject = rs.getObject("cast");

                Instant creationDate = Instant.parse(creationDateString);

                HashMap<String, String> resultMap = new HashMap<>();
                JSONObject object = (JSONObject) JSONValue.parse(resultObject.toString());

                for (Object keyString : object.keySet()) {
                    Object keyValue = object.get(keyString);
                    if (object.get(keyString) != null) {
                        resultMap.put(keyString.toString(), keyValue.toString());
                    }
                }

                JSONArray crewArray = (JSONArray) JSONValue.parse(crewObject.toString());
                ArrayList<Object> crew = new ArrayList<>();
                if (crewArray != null) {
                    crew.addAll(crewArray);
                }
                JSONArray castArray = (JSONArray) JSONValue.parse(castObject.toString());
                ArrayList<Object> cast = new ArrayList<>();
                if (castArray != null) {
                    cast.addAll(castArray);
                }

                Search newSearch = new Search(title, year, returnValue, creationDate);
                newSearch.setId(id);
                newSearch.setResults(resultMap);
                newSearch.setCrew(crew);
                newSearch.setCast(cast);
                newSearch.setPoster(poster);

                newSearches.add(newSearch);
            }

            Search.setSearches(newSearches);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Search.getSearches();
    }

    public static int deleteSearchById(long searchId) {

        int rowCount = 0;

        String sql = "DELETE FROM moviesearch WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, searchId);
            rowCount = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }
}
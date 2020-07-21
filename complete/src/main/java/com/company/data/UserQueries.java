package com.company.data;

import com.company.data.model.ApplicationConfig;
import com.company.data.model.User;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;

public class UserQueries {

    private static final ApplicationConfig applicationConfig = new ApplicationConfig();
    private static final String url = applicationConfig.getPostgresUrl();
    private static final String user = applicationConfig.getPostgresUser();
    private static final String password = applicationConfig.getPostgresPassword();

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static int insertUser(User user) {

        int rowCount = 0;

        String name = user.getName();
        String password = user.getPassword();
        String emailAddress = user.getEmailAddress();
        Boolean isValidated = user.getValidated();
        Boolean isRegistered = user.getRegistered();
        int validationAttempts = user.getValidationAttempts();
        String token = user.getToken();
        Instant tokenExpiryDate = user.getTokenExpiryDate();

        Timestamp timestamp = new Timestamp(tokenExpiryDate.toEpochMilli());

        try (Connection conn = connect()) {

            long userId;
            String role;

            String sql1 = "INSERT INTO users (name, password, emailaddress, isvalidated, isregistered, validationattempts, token, tokenExpiryDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, name);
                stmt.setString(2, password);
                stmt.setString(3, emailAddress);
                stmt.setBoolean(4, isValidated);
                stmt.setBoolean(5, isRegistered);
                stmt.setInt(6, validationAttempts);
                stmt.setString(7, token);
                stmt.setTimestamp(8, timestamp);
                stmt.executeUpdate();

                ResultSet keys = stmt.getGeneratedKeys();
                keys.next();

                userId = keys.getLong(1);
                role = "User";
            }

            String sql2 = "INSERT INTO roles (user_id, role) VALUES(?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                stmt.setLong(1, userId);
                stmt.setString(2, role);
                rowCount = stmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int insertAdmin(User user) {

        int rowCount = 0;

        String name = user.getName();
        String password = user.getPassword();
        String emailAddress = user.getEmailAddress();
        Boolean isValidated = user.getValidated();
        Boolean isRegistered = user.getRegistered();
        int validationAttempts = user.getValidationAttempts();
        String token = user.getToken();
        Instant tokenExpiryDate = user.getTokenExpiryDate();

        Timestamp timestamp = new Timestamp(tokenExpiryDate.toEpochMilli());

        try (Connection conn = connect()) {

            long userId;
            String role;

            String sql = "INSERT INTO users (name, password, emailaddress, isvalidated, isregistered, validationattempts, token, tokenExpiryDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, name);
                stmt.setString(2, password);
                stmt.setString(3, emailAddress);
                stmt.setBoolean(4, isValidated);
                stmt.setBoolean(5, isRegistered);
                stmt.setInt(6, validationAttempts);
                stmt.setString(7, token);
                stmt.setTimestamp(8, timestamp);
                stmt.executeUpdate();

                ResultSet keys = stmt.getGeneratedKeys();
                keys.next();

                userId = keys.getLong(1);
                role = "Admin";
            }

            String sql2 = "INSERT INTO roles (user_id, role) VALUES(?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                stmt.setLong(1, userId);
                stmt.setString(2, role);
                rowCount = stmt.executeUpdate();
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

        int rowCount = 0;

        String sql = "DELETE FROM users WHERE id = ?";

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

        int userCount = 0;

        String sql = "SELECT count(*) FROM users";

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
}

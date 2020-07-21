package com.company.data;

import com.company.data.model.ApplicationConfig;
import com.company.data.model.Email;
import com.company.data.model.User;

import java.sql.*;
import java.util.ArrayList;

public class EmailQueries {

    private static final ApplicationConfig applicationConfig = new ApplicationConfig();
    private static final String url = applicationConfig.getPostgresUrl();
    private static final String user = applicationConfig.getPostgresUser();
    private static final String password = applicationConfig.getPostgresPassword();

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static int insertEmail(Email email, User user) {

        int rowCount = 0;

        String type = email.getType();
        String subject = email.getSubject();
        String body = email.getBody();

        try (Connection conn = connect()) {

            long emailId;
            long userId;

            String sql1 = "INSERT INTO email (type, subject, body) VALUES(?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, type);
                stmt.setString(2, subject);
                stmt.setString(3, body);
                stmt.executeUpdate();

                ResultSet keys = stmt.getGeneratedKeys();
                keys.next();

                emailId = keys.getLong(1);
                userId = user.getId();
            }

            String sql2 = "INSERT INTO email_users (email_id, user_id) VALUES(?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                stmt.setLong(1, emailId);
                stmt.setLong(2, userId);
                rowCount = stmt.executeUpdate();
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

        int rowCount = 0;

        String sql = "DELETE FROM email WHERE id = ?";

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

        int emailCount = 0;

        String sql = "SELECT count(*) FROM email";

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
}

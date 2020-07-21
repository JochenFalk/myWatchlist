package com.company.data;

import com.company.data.model.ApplicationConfig;
import com.company.data.model.User;
import com.company.data.model.Watchlist;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.sql.*;
import java.util.ArrayList;

public class ListQueries {

    private static final ApplicationConfig applicationConfig = new ApplicationConfig();
    private static final String url = applicationConfig.getPostgresUrl();
    private static final String user = applicationConfig.getPostgresUser();
    private static final String password = applicationConfig.getPostgresPassword();

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static int insertList(Watchlist watchlist, User user) {

        int rowCount = 0;

        String title = watchlist.getTitle();
        String description = watchlist.getDescription();
        ArrayList<Object> listItems = watchlist.getListItems();

        JSONArray obj = new JSONArray();
        if (listItems != null) {
            obj.addAll(listItems);
        }

        try (Connection conn = connect()) {

            long listId;
            long userId;

            String sql1 = "INSERT INTO watchlist (title, description, listitems) VALUES(?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, title);
                stmt.setString(2, description);
                stmt.setObject(3, obj.toJSONString());
                stmt.executeUpdate();

                ResultSet keys = stmt.getGeneratedKeys();
                keys.next();

                listId = keys.getLong(1);
                userId = user.getId();
            }

            String sql2 = "INSERT INTO watchlist_users (watchlist_id, user_id) VALUES(?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                stmt.setLong(1, listId);
                stmt.setLong(2, userId);
                rowCount = stmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int updateList(Watchlist watchlist) {

        int rowCount = 0;

        long id = watchlist.getId();
        String title = watchlist.getTitle();
        String description = watchlist.getDescription();
        ArrayList listItems = watchlist.getListItems();

        JSONArray obj = new JSONArray();
        obj.addAll(listItems);

        String sql = "UPDATE watchlist SET title = ?, description = ?, listitems = ? WHERE id = ?";

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

        int rowCount = 0;
        long id = watchlist.getId();

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
        String userName = user.getName();

        String sql = "select wu.watchlist_id, w.title, w.description, w.listItems\n" +
                "from watchlist_users wu\n" +
                "join users u on wu.user_id = u.id\n" +
                "join watchlist w on wu.watchlist_id = w.id\n" +
                "where u.name = ?\n" +
                "order by wu.watchlist_id";

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

            if (rs.next()) {
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
}

package com.company.data;

import com.company.data.model.ApplicationConfig;
import com.company.data.model.Search;
import com.company.data.model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieSearchQueries {

    private static final ApplicationConfig applicationConfig = new ApplicationConfig();
    private static final String url = applicationConfig.getPostgresUrl();
    private static final String user = applicationConfig.getPostgresUser();
    private static final String password = applicationConfig.getPostgresPassword();

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static int insertSearch(Search search, User user) {

        int rowCount = 0;

        String title = search.getTitle();
        String year = search.getYear();
        byte[] poster = search.getPoster();
        int returnValue = search.getReturnValue();
        String creationDate = search.getCreationDate().toString();

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

            long searchId;
            long userId;

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
                rowCount = stmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public static int updateSearch(Search search) {

        int rowCount = 0;

        long id = search.getId();
        String title = search.getTitle();
        String year = search.getYear();
        byte[] poster = search.getPoster();
        int returnValue = search.getReturnValue();
        String creationDate = search.getCreationDate().toString();

        String sql = "UPDATE moviesearch SET title = ?, year = ?, poster = ?, returnvalue = ?, creationdate = ? WHERE id = ?";

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

        String sql = "select ms.id, ms.title, ms.poster, ms.year, ms.returnvalue, ms.creationdate, sr.value as results, ct.value as movieCast, cw.value as movieCrew\n" +
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

            if (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String year = rs.getString("year");
                byte[] poster = rs.getBytes("poster");
                int returnValue = rs.getInt("returnvalue");
                String creationDateString = rs.getString("creationdate");
                Object resultObject = rs.getObject("results");
                Object crewObject = rs.getObject("movieCrew");
                Object castObject = rs.getObject("movieCast");

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

        String sql = "select ms.id, ms.title, ms.year, ms.poster, ms.returnvalue, ms.creationdate, sr.value as results, ct.value as movieCast, cw.value as movieCrew\n" +
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
                Object crewObject = rs.getObject("movieCrew");
                Object castObject = rs.getObject("movieCast");

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

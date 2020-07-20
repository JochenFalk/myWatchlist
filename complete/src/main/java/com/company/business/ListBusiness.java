package com.company.business;

import com.company.data.PostgreSystemQueries;
import com.company.data.model.User;
import com.company.data.model.Watchlist;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ListBusiness {

    private static final String API_KEY = "1c0a5b80ba526a387cb22c7d79fbeb03";
    private static final int EXPIRY_TIME = 60 * 60; // 1 hour
    private static final int MAX_LIST = 10;
    private static final int MAX_PAGE_NUMBER = 5;

    public static void createPublicList(String title, String description, ArrayList<Object> listItems, User user) {
        Watchlist watchlist = new Watchlist(title, description, listItems);
        PostgreSystemQueries.insertList(watchlist, user);
    }

    public static Watchlist createSystemList(String title, ArrayList<Object> listItems) {
        Instant now = Instant.now();
        return new Watchlist(title, now.toString(), listItems); //set instant as description as temporary workaround for missing field in DB
    }

    public static void renameList(String title, User user) {
        ArrayList<Watchlist> watchlists = PostgreSystemQueries.getAllListsByUser(user);
        for (Watchlist thisWatchlist : watchlists) {
            boolean exists = thisWatchlist.getTitle().equals(title);
            if (exists) {
                thisWatchlist.setTitle(title);
            }
        }
    }

    public static Watchlist getPopular() {

        ArrayList<Object> newListItems = new ArrayList<>();
        int page = getRandom();
        int count = 0;

        String SearchURL = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY + "&language=en-US&query=&page=" + page;
        HttpGet get = new HttpGet(SearchURL);

        // set connection time out on request level.
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        get.setConfig(requestConfig);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(get)) {

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String string = EntityUtils.toString(entity); // parse response to string (output is JSON)
                JSONObject object = (JSONObject) JSONValue.parse(string); // create JObject with results
                JSONArray array = (JSONArray) object.get("results"); // get array "results"

                int i = 0;
                while (count <= MAX_LIST && i < array.size()) {

                    HashMap<String, String> resultMap = new HashMap<>();
                    JSONObject result = (JSONObject) array.get(i);

                    if (result.get("poster_path") != null) {

                        String id = result.get("id").toString();
                        String title = result.get("title").toString();
                        String[] releaseDate = result.get("release_date").toString().split("-");
                        String year = releaseDate[0];

                        resultMap.put("id", id);
                        resultMap.put("title", title);
                        resultMap.put("release_year", year);

                        newListItems.add(resultMap);
                        count++;
                    }
                    i++;
                }

                return ListBusiness.createSystemList("Popular", newListItems);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Watchlist getTopRated() {

        ArrayList<Object> newListItems = new ArrayList<>();
        int page = getRandom();
        int count = 0;

        String searchURL = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY + "&language=en-US&query=&page=" + page;
        HttpGet get = new HttpGet(searchURL);

        // set connection time out on request level.
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        get.setConfig(requestConfig);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(get)) {

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String string = EntityUtils.toString(entity); // parse response to string (output is JSON)
                JSONObject object = (JSONObject) JSONValue.parse(string); // create JObject with results
                JSONArray array = (JSONArray) object.get("results"); // get array "results"

                int i = 0;
                while (count <= MAX_LIST && i < array.size()) {

                    HashMap<String, String> resultMap = new HashMap<>();
                    JSONObject result = (JSONObject) array.get(i);

                    if (result.get("poster_path") != null) {

                        String id = result.get("id").toString();
                        String title = result.get("title").toString();
                        String[] releaseDate = result.get("release_date").toString().split("-");
                        String year = releaseDate[0];

                        resultMap.put("id", id);
                        resultMap.put("title", title);
                        resultMap.put("release_year", year);

                        newListItems.add(resultMap);
                        count++;
                    }
                    i++;
                }

                return ListBusiness.createSystemList("TopRated", newListItems);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Watchlist getSimilar(int movieId) {

        ArrayList<Object> newListItems = new ArrayList<>();
//        int page = getRandom();
        int page = 1;
        int count = 0;

        String searchURL = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=" + API_KEY + "&language=en-US&query=&page=" + page;
        HttpGet get = new HttpGet(searchURL);

        // set connection time out on request level.
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        get.setConfig(requestConfig);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(get)) {

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String string = EntityUtils.toString(entity); // parse response to string (output is JSON)
                JSONObject object = (JSONObject) JSONValue.parse(string); // create JObject with results
                JSONArray array = (JSONArray) object.get("results"); // get array "results"

                int i = 0;
                while (count <= MAX_LIST && i < array.size()) {

                    HashMap<String, String> resultMap = new HashMap<>();
                    JSONObject result = (JSONObject) array.get(i);

                    if (result.get("poster_path") != null) {

                        String id = result.get("id").toString();
                        String title = result.get("title").toString();
                        String[] releaseDate = result.get("release_date").toString().split("-");
                        String year = releaseDate[0];

                        resultMap.put("id", id);
                        resultMap.put("title", title);
                        resultMap.put("release_year", year);

                        newListItems.add(resultMap);
                        count++;
                    }
                    i++;
                }

                return ListBusiness.createSystemList("Similar", newListItems);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Watchlist getSystemList(String listTitle, String userName) {
        Watchlist watchlist = PostgreSystemQueries.getListByTitleAndUser(listTitle, userName);
        User system = UserBusiness.getUserById("1");
        if (watchlist != null) {
            Instant dateCreated = Instant.parse(watchlist.getDescription());
            Instant expiryDate = dateCreated.plusSeconds(EXPIRY_TIME);
            if (expiryDate.isBefore(Instant.now())) {
                if (listTitle.equals("Popular")) {
                    Watchlist newWatchlist = getPopular();
                    if (newWatchlist != null) {
                        newWatchlist.setId(watchlist.getId());
                        PostgreSystemQueries.updateList(newWatchlist);
                        return newWatchlist;
                    }
                }
                if (listTitle.equals("TopRated")) {
                    Watchlist newWatchlist = getTopRated();
                    if (newWatchlist != null) {
                        newWatchlist.setId(watchlist.getId());
                        PostgreSystemQueries.updateList(newWatchlist);
                        return newWatchlist;
                    }
                }
            } else {
                return watchlist;
            }
        } else {
            if (listTitle.equals("Popular")) {
                Watchlist newWatchlist = getPopular();
                if (newWatchlist != null) {
                    PostgreSystemQueries.insertList(newWatchlist, system);
                    return newWatchlist;
                }
            }
            if (listTitle.equals("TopRated")) {
                Watchlist newWatchlist = getTopRated();
                if (newWatchlist != null) {
                    PostgreSystemQueries.insertList(newWatchlist, system);
                    return newWatchlist;
                }
            }
        }
        return null;
    }

    public static Watchlist getListFromUser(String listTitle, HttpSession session) {
        if (session.getAttribute("username") != null) {
            String userName = session.getAttribute("username").toString();
            Watchlist watchlist = PostgreSystemQueries.getListByTitleAndUser(listTitle, userName);
            if (watchlist != null) {
                return watchlist;
            }
        }
        return null;
    }

    public static ArrayList<Watchlist> getAllListsFromUser(HttpSession session) {
        if (session.getAttribute("username") != null) {
            String userName = session.getAttribute("username").toString();
            User user = PostgreSystemQueries.getUserByName(userName);
            if (user != null) {
                ArrayList<Watchlist> arrayList = PostgreSystemQueries.getAllListsByUser(user);
                if (arrayList != null) {
                    return arrayList;
                }
            }

        }
        return null;
    }

    public static Boolean setPushedMovie(String pushedMovie, HttpSession session) {
        if (session.getAttribute("username") != null) {
            session.setAttribute("pushedmovie", pushedMovie);
            return true;
        }
        return false;
    }

    public static String getPushedMovie(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return session.getAttribute("pushedmovie").toString();
        }
        return null;
    }

    public static Boolean setCurrentList(String listTitle, HttpSession session) {
        if (session.getAttribute("username") != null) {
            session.setAttribute("currentlist", listTitle);
            return true;
        }
        return false;
    }

    public static Watchlist getCurrentList(HttpSession session) {
        if (session.getAttribute("username") != null) {
            String userName = session.getAttribute("username").toString();
            if (session.getAttribute("currentlist") != null) {
                String currentList = session.getAttribute("currentlist").toString();
                return PostgreSystemQueries.getListByTitleAndUser(currentList, userName);
            } else {
                ArrayList<Watchlist> arrayList = getAllListsFromUser(session);
                Watchlist watchlist;
                if (arrayList != null && arrayList.size() != 0) {
                    watchlist = arrayList.get(0);
                    setCurrentList(watchlist.getTitle(), session);
                } else {
                    watchlist = new Watchlist("MyWatchlist", "My first watchList");
                    User user = PostgreSystemQueries.getUserByName(userName);
                    PostgreSystemQueries.insertList(watchlist, user);
                }
                return watchlist;
            }
        }
        return null;
    }

    public static String createList(String listTitle, String listDesc, HttpSession session) {
        if (session.getAttribute("username") != null) {
            String userName = session.getAttribute("username").toString();
            User user = PostgreSystemQueries.getUserByName(userName);
            if (user != null) {
                Watchlist watchlist = PostgreSystemQueries.getListByTitleAndUser(listTitle, userName);
                System.out.println(watchlist);
                if (watchlist == null) {
                    Watchlist newWatchlist = new Watchlist(listTitle, listDesc);
                    PostgreSystemQueries.insertList(newWatchlist, user);
                    return "true";
                } else {
                    JSONObject msg = new JSONObject();
                    msg.put("msg", "A list with title \"" + listTitle + "\" already exists. Please choose another title.");
                    return msg.toString();
                }
            }
        }
        return"false";
    }

    public static Boolean createListWithItems(String listTitle, String listDesc, String listItems, HttpSession session) {
        if (session.getAttribute("username") != null) {

            JSONArray array = (JSONArray) JSONValue.parse(listItems);
            ArrayList<Object> arrayList = convertToArraylist(array);

            String userName = session.getAttribute("username").toString();
            Watchlist watchlist = new Watchlist(listTitle, listDesc, arrayList);
            User user = PostgreSystemQueries.getUserByName(userName);
            if (user != null) {
                PostgreSystemQueries.insertList(watchlist, user);
                return true;
            }
        }
        return false;
    }

    public static Boolean updateListFromUser(String listTitle, String listItems, HttpSession session) {
        if (session.getAttribute("username") != null) {

            JSONArray array = (JSONArray) JSONValue.parse(listItems);
            ArrayList<Object> arrayList = convertToArraylist(array);

            String userName = session.getAttribute("username").toString();
            Watchlist watchlist = PostgreSystemQueries.getListByTitleAndUser(listTitle, userName);
            if (watchlist != null) {
                watchlist.setListItems(arrayList);
                PostgreSystemQueries.updateList(watchlist);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static Boolean deleteListFromUser(String title, HttpSession session) {
        if (session.getAttribute("username") != null) {
            String userName = session.getAttribute("username").toString();
            Watchlist watchlist = PostgreSystemQueries.getListByTitleAndUser(title, userName);
            if (watchlist != null) {
                PostgreSystemQueries.deleteList(watchlist);
                session.removeAttribute("currentlist");
                return true;
            }
        }
        return false;
    }

    public static void modifyListDescription(String description, User user) {
        ArrayList<Watchlist> watchlists = PostgreSystemQueries.getAllListsByUser(user);
        for (Watchlist thisWatchlist : watchlists) {
            boolean exists = thisWatchlist.getTitle().equals(description);
            if (exists) {
                thisWatchlist.setDescription(description);
            }
        }
    }

    private static int getRandom() {
        Random random = new Random();
        return random.nextInt(MAX_PAGE_NUMBER) + 1;
    }

    private static ArrayList<Object> convertToArraylist(JSONArray array) {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0, l = array.size(); i < l; i++) {
            list.add(array.get(i));
        }
        return list;
    }

    private static float clamp(float val, float min, float max) {
        return Math.min(Math.max(val, min), max);
    }
}

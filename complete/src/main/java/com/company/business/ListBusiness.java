package com.company.business;

import com.company.data.ListQueries;
import com.company.data.UserQueries;
import com.company.data.model.ApplicationConfig;
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

    private static final String TMDB_API_KEY = ApplicationConfig.getTmdbApiKey();
    private static final int LIST_EXPIRY_TIME = ApplicationConfig.getListExpiryTime();
    private static final int MAX_LIST_ITEMS = ApplicationConfig.getMaxListItems();
    private static final int MAX_PAGE_NUMBER = ApplicationConfig.getMaxPageNumber();

    public static Watchlist createSystemList(String title, ArrayList<Object> listItems) {
        Instant now = Instant.now();
        return new Watchlist(title, now.toString(), listItems); //set instant as description as temporary workaround for missing field in DB
    }

    public static Watchlist getPopular() {

        int count = 0;
        int page = getRandom();

        ArrayList<Object> newListItems = new ArrayList<>();

        String SearchURL = "https://api.themoviedb.org/3/movie/popular?api_key=" + TMDB_API_KEY + "&language=en-US&query=&page=" + page;
        HttpGet get = new HttpGet(SearchURL);

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
                while (count <= MAX_LIST_ITEMS && i < array.size()) {

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

        int count = 0;
        int page = getRandom();

        ArrayList<Object> newListItems = new ArrayList<>();

        String searchURL = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + TMDB_API_KEY + "&language=en-US&query=&page=" + page;
        HttpGet get = new HttpGet(searchURL);

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
                while (count <= MAX_LIST_ITEMS && i < array.size()) {

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

        int page = 1;
        int count = 0;

        ArrayList<Object> newListItems = new ArrayList<>();

        String searchURL = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=" + TMDB_API_KEY + "&language=en-US&query=&page=" + page;
        HttpGet get = new HttpGet(searchURL);

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
                while (count <= MAX_LIST_ITEMS && i < array.size()) {

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

        Watchlist watchlist = ListQueries.getListByTitleAndUser(listTitle, userName);
        User system = UserQueries.getUserByName(userName);

        if (watchlist != null) {

            Instant dateCreated = Instant.parse(watchlist.getDescription());
            Instant expiryDate = dateCreated.plusSeconds(LIST_EXPIRY_TIME);

            if (expiryDate.isBefore(Instant.now())) {
                if (listTitle.equals("Popular")) {
                    Watchlist newWatchlist = getPopular();
                    if (newWatchlist != null) {
                        newWatchlist.setId(watchlist.getId());
                        ListQueries.updateList(newWatchlist);
                        return newWatchlist;
                    }
                }
                if (listTitle.equals("TopRated")) {
                    Watchlist newWatchlist = getTopRated();
                    if (newWatchlist != null) {
                        newWatchlist.setId(watchlist.getId());
                        ListQueries.updateList(newWatchlist);
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
                    ListQueries.insertList(newWatchlist, system);
                    return newWatchlist;
                }
            }
            if (listTitle.equals("TopRated")) {
                Watchlist newWatchlist = getTopRated();
                if (newWatchlist != null) {
                    ListQueries.insertList(newWatchlist, system);
                    return newWatchlist;
                }
            }
        }
        return null;
    }

    public static Watchlist getListFromUser(String listTitle, HttpSession session) {
        if (session.getAttribute("username") != null) {
            String userName = session.getAttribute("username").toString();
            return ListQueries.getListByTitleAndUser(listTitle, userName);
        }
        return null;
    }

    public static ArrayList<Watchlist> getAllListsFromUser(HttpSession session) {
        User user = UserBusiness.getUserFromSession(session);
        if (user != null) {
            return ListQueries.getAllListsByUser(user);
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
                return ListQueries.getListByTitleAndUser(currentList, userName);
            } else {
                ArrayList<Watchlist> arrayList = getAllListsFromUser(session);
                Watchlist watchlist;
                if (arrayList != null && arrayList.size() != 0) {
                    watchlist = arrayList.get(0);
                    setCurrentList(watchlist.getTitle(), session);
                } else {
                    watchlist = new Watchlist("MyWatchlist", "My first watchList");
                    User user = UserQueries.getUserByName(userName);
                    ListQueries.insertList(watchlist, user);
                }
                return watchlist;
            }
        }
        return null;
    }

    public static String createList(String listTitle, String listDesc, HttpSession session) {
        User user = UserBusiness.getUserFromSession(session);
        if (user != null) {
            Watchlist watchlist = ListQueries.getListByTitleAndUser(listTitle, user.getName());
            if (watchlist == null) {
                Watchlist newWatchlist = new Watchlist(listTitle, listDesc);
                ListQueries.insertList(newWatchlist, user);
                return "true";
            } else {
                net.minidev.json.JSONObject msg = new net.minidev.json.JSONObject();
                msg.put("msg", "A list with title \"" + listTitle + "\" already exists. Please choose another title.");
                return msg.toString();
            }
        }
        return "false";
    }

    public static Boolean createListWithItems(String listTitle, String listDesc, String listItems, HttpSession session) {

        JSONArray array = (JSONArray) JSONValue.parse(listItems);
        ArrayList<Object> arrayList = convertToArraylist(array);

        User user = UserBusiness.getUserFromSession(session);
        if (user != null) {
            Watchlist watchlist = ListQueries.getListByTitleAndUser(listTitle, user.getName());
            if (watchlist == null) {
                Watchlist newWatchlist = new Watchlist(listTitle, listDesc, arrayList);
                ListQueries.insertList(newWatchlist, user);
            } else {
                ArrayList<Object> existingList = watchlist.getListItems();
                existingList.addAll(arrayList);
                watchlist.setListItems(existingList);
                ListQueries.updateList(watchlist);
            }
            return true;
        }
        return false;
    }

    public static Boolean updateListFromUser(String listTitle, String listItems, HttpSession session) {
        if (session.getAttribute("username") != null) {

            JSONArray array = (JSONArray) JSONValue.parse(listItems);
            ArrayList<Object> arrayList = convertToArraylist(array);

            String userName = session.getAttribute("username").toString();
            Watchlist watchlist = ListQueries.getListByTitleAndUser(listTitle, userName);
            if (watchlist != null) {
                watchlist.setListItems(arrayList);
                ListQueries.updateList(watchlist);
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
            Watchlist watchlist = ListQueries.getListByTitleAndUser(title, userName);
            if (watchlist != null) {
                ListQueries.deleteList(watchlist);
                session.removeAttribute("currentlist");
                return true;
            }
        }
        return false;
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
}

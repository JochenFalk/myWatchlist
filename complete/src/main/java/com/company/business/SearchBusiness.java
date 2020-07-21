package com.company.business;

import com.company.data.MovieSearchQueries;
import com.company.data.model.ApplicationConfig;
import com.company.data.model.Search;
import com.company.data.model.User;
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

public class SearchBusiness {

    private static final String API_KEY = ApplicationConfig.getTmdbApiKey();
    private static final String BASE_IMAGE_URL = ApplicationConfig.getBaseImageUrl();
    private static final String DEFAULT_POSTER_URL = ApplicationConfig.getDefaultPosterUrl();
    private static final String BASE_BACKDROP_URL = ApplicationConfig.getBaseBackdropUrl();
    private static final String DEFAULT_BACKDROP_URL = ApplicationConfig.getDefaultBackdropUrl();
    private static final String BASE_CASTPOSTER_URL = ApplicationConfig.getBaseCastposterUrl();
    private static final String DEFAULT_CASTPOSTER_URL = ApplicationConfig.getDefaultCastposterUrl();
    private static final String BASE_VIDEO_URL = ApplicationConfig.getBaseVideoUrl();
    private static final String APPEND_VIDEO_URL = ApplicationConfig.getAppendVideoUrl();

    private static final int MAX_CAST_ITEMS = ApplicationConfig.getMaxCastItems();
    private static final int SEARCH_EXPIRY_TIME = ApplicationConfig.getSearchExpiryTime();

    public static Search newSearch(String title, String year, int returnValue) {
        return search(title, year, returnValue);
    }

    public static Search search(String title, String year, int returnValue) {

        Instant now = Instant.now();
        Search search = new Search(title, year, returnValue, now);

        String noSpacesTitle = title.replaceAll("\\s+", "%20");
        String titleSearchURL = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + noSpacesTitle + "&page=1&include_adult=false&year=" + year;
        HttpGet titleSearch = new HttpGet(titleSearchURL);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        titleSearch.setConfig(requestConfig);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(titleSearch)) {

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String string = EntityUtils.toString(entity); // parse response to string (output is JSON)
                JSONObject object = (JSONObject) JSONValue.parse(string); // create JObject with results
                JSONArray array = (JSONArray) object.get("results"); // get array "results"

                if (array.size() != 0 && (array.size() > returnValue)) {

                    JSONObject result = (JSONObject) array.get(returnValue); // create JObject with result
                    processSearch(search, result);
                    getExtendedSearch(search);

                    return search;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getExtendedSearch(Search search) {

        String id = search.getResults().get("id");
        String idSearchURL = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + API_KEY + "&append_to_response=credits,videos";
        HttpGet idSearch = new HttpGet(idSearchURL);

        // set connection time out on request level.
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        idSearch.setConfig(requestConfig);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(idSearch)) {

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String result = EntityUtils.toString(entity); // parse response to string (output is JSON)
                JSONObject obj = (JSONObject) JSONValue.parse(result); // create JObject with results
                JSONObject credits = (JSONObject) obj.get("credits"); // create JObject with credits
                JSONObject videos = (JSONObject) obj.get("videos"); // create JObject with videos
                JSONArray cast = (JSONArray) credits.get("cast"); // get array "cast"
                JSONArray crew = (JSONArray) credits.get("crew"); // get array "crew"

                processExtendedSearch(search, cast, crew, videos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSearch(Search search, JSONObject result) {

        HashMap<String, String> resultMap = new HashMap<>();

        search.setTitle(result.get("title").toString());
        String[] releaseDate = result.get("release_date").toString().split("-");
        String releaseYear = releaseDate[0];
        search.setYear(releaseYear);

        // set and check poster path
        String moviePoster = DEFAULT_POSTER_URL;
        if (result.get("poster_path") != null) {
            moviePoster = BASE_IMAGE_URL + result.get("poster_path").toString();
            if (moviePoster.endsWith("null")) {
                moviePoster = DEFAULT_POSTER_URL;
            }
        }
        // set and check backdrop path
        String movieBackDrop = DEFAULT_BACKDROP_URL;
        if (result.get("backdrop_path") != null) {
            movieBackDrop = BASE_BACKDROP_URL + result.get("backdrop_path").toString();
            if (movieBackDrop.endsWith("null")) {
                movieBackDrop = DEFAULT_BACKDROP_URL;
            }
        }

        resultMap.put("release_year", releaseYear);
        resultMap.put("poster_url", moviePoster);
        resultMap.put("backdrop_url", movieBackDrop);

        for (Object keyString : result.keySet()) {
            Object keyValue = result.get(keyString);
            if (result.get(keyString) != null) {
                resultMap.put(keyString.toString(), keyValue.toString());
            }
        }
        search.setResults(resultMap);
    }

    private static void processExtendedSearch(Search search, JSONArray cast, JSONArray crew, JSONObject videos) {

        ArrayList<Object> newCast = new ArrayList<>();
        ArrayList<Object> newCrew = new ArrayList<>();

        int listEnd = (int) clamp(cast.size(), cast.size(), MAX_CAST_ITEMS);

        for (int i = 0; i < listEnd; i++) {

            JSONObject castObject = (JSONObject) cast.get(i);
            HashMap<String, String> castMap = new HashMap<>();

            String id = castObject.get("id").toString();
            String name = castObject.get("name").toString();
            String character = castObject.get("character").toString();

            String profilePathUrl = DEFAULT_CASTPOSTER_URL;

            if (castObject.get("profile_path") != null) {
                String profilePath = castObject.get("profile_path").toString();
                if (!profilePath.isEmpty()) {
                    profilePathUrl = BASE_CASTPOSTER_URL + profilePath;
                }
            }

            castMap.put("id", id);
            castMap.put("name", name);
            castMap.put("character", character);
            castMap.put("profile_url", profilePathUrl);

            newCast.add(castMap);
        }

        for (Object thisObject : crew) {

            JSONObject thisCrew = (JSONObject) thisObject;
            HashMap<String, String> crewMap = new HashMap<>();

            String id = thisCrew.get("id").toString();
            String name = thisCrew.get("name").toString();
            String job = thisCrew.get("job").toString();

            if (job.equals("Director") || job.equals("Writer")) {

                String profilePathUrl = DEFAULT_CASTPOSTER_URL;

                if (thisCrew.get("profile_path") != null) {
                    String profilePath = thisCrew.get("profile_path").toString();
                    if (!profilePath.isEmpty()) {
                        profilePathUrl = BASE_CASTPOSTER_URL + profilePath;
                    }
                }

                crewMap.put("id", id);
                crewMap.put("name", name);
                crewMap.put("job", job);
                crewMap.put("profile_url", profilePathUrl);

                newCrew.add(crewMap);
            }
        }

        String videoURL;
        JSONArray array = (JSONArray) videos.get("results");

        if (array.size() != 0) {
            JSONObject result = (JSONObject) array.get(0);
            if (result.get("key") != null) {

                HashMap<String, String> results = search.getResults();
                HashMap<String, String> resultMap = new HashMap<>();

                videoURL = BASE_VIDEO_URL + result.get("key").toString() + APPEND_VIDEO_URL;

                resultMap.put("video_url", videoURL);

                for (String keyString : results.keySet()) {
                    String keyValue = results.get(keyString);
                    if (results.get(keyString) != null) {
                        resultMap.put(keyString, keyValue);
                    }
                }
                search.setResults(resultMap);
            }
        }
        search.setCast(newCast);
        search.setCrew(newCrew);
    }

    public static Search retrieveSearch(String title, String year) {
        Search search = MovieSearchQueries.getSearch(title, year);
        if (search != null) {
            Instant creationDate = search.getCreationDate();
            Instant expiryDate = creationDate.plusSeconds(SEARCH_EXPIRY_TIME);
            if (expiryDate.isBefore(Instant.now())) {
                // delete the search so next call it will be recreated
                // then return search stored in "search"
                MovieSearchQueries.deleteSearchById(search.getId());
            }
            return search;
        }
        return null;
    }

    public static void saveSearch(String searchString, HttpSession session) {

        User user = UserBusiness.getUserFromSession(session);

        if (user != null) {

            JSONObject searchObject = (JSONObject) JSONValue.parse(searchString);

            String title = searchObject.get("title").toString();
            String year = searchObject.get("year").toString();
            int returnValue = Integer.parseInt(searchObject.get("returnValue").toString());
            Instant creationDate = Instant.parse(searchObject.get("creationDate").toString());

            Search existingSearch = MovieSearchQueries.getSearch(title, year);
            if (existingSearch == null) {

                Search search = new Search(title, year, returnValue, creationDate);

                JSONObject results = (JSONObject) searchObject.get("results");
                HashMap<String, String> resultMap = new HashMap<>();
                for (Object keyString : results.keySet()) {
                    Object keyValue = results.get(keyString);
                    if (results.get(keyString) != null) {
                        resultMap.put(keyString.toString(), keyValue.toString());
                    }
                }

                JSONArray cast = (JSONArray) searchObject.get("cast");
                ArrayList<Object> newCast = new ArrayList<>();
                newCast.addAll(cast);

                JSONArray crew = (JSONArray) searchObject.get("crew");
                ArrayList<Object> newCrew = new ArrayList<>();
                newCrew.addAll(crew);

                String inputUrl = results.get("poster_url").toString();
                byte[] poster = ImageConverter.convertToThumb(inputUrl);

                search.setResults(resultMap);
                search.setCast(newCast);
                search.setCrew(newCrew);
                search.setPoster(poster);

                MovieSearchQueries.insertSearch(search, user);
            }
        }
    }

    public static float clamp(float val, float min, float max) {
        return Math.min(Math.max(val, min), max);
    }
}

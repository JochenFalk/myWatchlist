package com.company.business;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.util.Properties;

public class SearchBusiness {

    private static int results;
    private Properties movieProperties = new Properties();
    private static final String API_KEY = "1c0a5b80ba526a387cb22c7d79fbeb03";

    public static String searchByTitle(String title, String year) {

        String regexTitle = title.replaceAll("\\s","%20");
        String requestURL = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + regexTitle + "&page=1&include_adult=false&year=" + year;
        HttpGet request = new HttpGet(requestURL);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

//            System.out.println(response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String result = EntityUtils.toString(entity);
                JSONObject obj = (JSONObject) JSONValue.parse(result);
                JSONArray arr = (JSONArray)obj.get("results");
                JSONObject results = (JSONObject) arr.get(0);

                System.out.println(results.get("title"));

//                Search search = new Search(title, searchYear);

                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String searchById(String id) {

        String requestURL = "https://api.themoviedb.org/3/movie/" + id + "/credits?api_key=" + API_KEY;
        HttpGet request = new HttpGet(requestURL);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

//            System.out.println(response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println(result);
                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}

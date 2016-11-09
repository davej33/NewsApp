package com.example.android.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsapp.ArticleList.LOG_TAG;

/**
 * Created by dnj on 11/8/16.
 */

public class QueryUtils {

    public static List<Article> fetchData(String urlString) {
        // convert url string to URL object
        URL url = createURL(urlString);


        String jsonResponse = "";

        // run url and store results in string
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "http request error", e);
        }

        // filter for required data and store in List
        List<Article> articleList = extractData(jsonResponse);

        return articleList;
    }

    // convert url
    private static URL createURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "malformed url", e);
        }
        return url;
    }

    // http request
    private static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream;
        String jsonResponse = "";
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "connection error", e);
        }
        return jsonResponse;
    }

    // stream reader
    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "input stream reader error", e);
        }
        return output.toString();
    }

    private static List<Article> extractData(String data) {

        List<Article> articleList = new ArrayList<>();

        if (data != null) {

            try {
                JSONObject root = new JSONObject(data);
                JSONObject response = root.getJSONObject("response");
                JSONArray results = response.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject element = results.getJSONObject(i);

                    String title = "";
                    String section = "";
                    String date = "";
                    String url = "";

                    try {
                        if (element.has("webTitle")) {
                            title = element.getString("webTitle");
                        }

                        if (element.has("sectionName")) {
                            section = element.getString("sectionName");}

                        if (element.has("webPublicationDate")){
                        date = element.getString("webPublicationDate");}

                        if (element.has("webUrl")){
                        url = element.getString("webUrl");}

                    } catch (Error e) {
                        Log.e(LOG_TAG, "error", e);
                    }

                    Article article = new Article(title, section, date, url);

                    articleList.add(article);
                }


            } catch (JSONException e) {
                Log.e(LOG_TAG, "json parse error", e);
            }
        }

        return articleList;
    }
}

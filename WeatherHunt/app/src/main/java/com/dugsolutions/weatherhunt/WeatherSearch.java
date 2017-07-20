package com.dugsolutions.weatherhunt;

import android.net.Uri;

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
import java.util.TimeZone;

import timber.log.Timber;

/**
 * Created by dug on 7/20/17.
 */

public class WeatherSearch extends WeatherBase {

    static final String AUTHORITY = "api.worldweatheronline.com";

    public WeatherSearchResult query(String location) {
        WeatherSearchResult weatherResult = null;
        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(AUTHORITY)
                    .appendPath("premium")
                    .appendPath("v1")
                    .appendPath("query.ashx")
                    .appendQueryParameter("query", location)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("timezone", "yes")
                    .appendQueryParameter("key", KEY);
            Uri uri = builder.build();
            URL url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String result = getResult(connection);
            connection.disconnect();
            if (result == null) {
                return null;
            }
            weatherResult = new WeatherSearchResult();
            JSONObject object = new JSONObject(result);
            JSONObject root = object.getJSONObject("search_api");
            JSONArray elements = root.getJSONArray("result");
            for (int i = 0; i < elements.length(); i++) {
                JSONObject ele = elements.getJSONObject(i);
                WeatherSearchResult.Location loc = new WeatherSearchResult.Location();
                loc.areaName = getValue(ele, "areaName");
                loc.country = getValue(ele, "country");
                loc.region = getValue(ele, "region");
                loc.latitude = ele.getString("latitude");
                loc.longitude = ele.getString("longitude");
                loc.population = ele.getInt("population");
                loc.timeZone = getTimeZone(ele, "timezone");
                loc.url = getURL(ele, "weatherUrl");
                weatherResult.add(loc);
            }
        } catch (Exception ex) {
            Timber.e(ex);
        }
        return weatherResult;
    }

    String getResult(HttpURLConnection connection) throws IOException
    {
        InputStream inputStream;
        inputStream = connection.getInputStream();
        if (inputStream == null) {
            Timber.e("NULL response from server");
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sbuf = new StringBuilder();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            if (sbuf.length() > 0) {
                sbuf.append("\n");
            }
            sbuf.append(inputLine);
        }
        inputStream.close();
        return sbuf.toString();
    }

    String getValue(JSONObject obj, String arrayName) throws JSONException {
        JSONArray array = obj.getJSONArray(arrayName);
        if (array.length() > 0) {
            JSONObject sub = array.getJSONObject(0);
            return sub.getString("value");
        }
        Timber.e("Unexpected: no entries for " + arrayName);
        return null;
    }

    URL getURL(JSONObject obj, String arrayName) throws JSONException, MalformedURLException {
        String value = getValue(obj, arrayName);
        if (value != null) {
            return new URL(value);
        }
        return null;
    }

    TimeZone getTimeZone(JSONObject obj, String objName) throws JSONException {
        JSONObject sub = obj.getJSONObject(objName);
        if (sub == null) {
            return null;
        }
        if (!sub.has("offset")) {
            return null;
        }
        int offset = sub.getInt("offset");
        StringBuilder sbuf = new StringBuilder();
        sbuf.append("+");
        sbuf.append(offset);
        return TimeZone.getTimeZone(sbuf.toString());
    }
}

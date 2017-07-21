package com.dugsolutions.weatherhunt.data;

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

public class WeatherBase {

    protected static final String AUTHORITY = "api.worldweatheronline.com";
    protected static final String KEY = "34c23c88e7314574828220153171907";

    protected String post(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String result = getResult(connection);
        connection.disconnect();
        return result;
    }

    protected String getResult(HttpURLConnection connection) throws IOException {
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

    protected String getValue(JSONObject obj, String arrayName) throws JSONException {
        JSONArray array = obj.getJSONArray(arrayName);
        if (array.length() > 0) {
            JSONObject sub = array.getJSONObject(0);
            return sub.getString("value");
        }
        Timber.e("Unexpected: no entries for " + arrayName);
        return null;
    }

    protected URL getURL(JSONObject obj, String arrayName) throws JSONException, MalformedURLException {
        String value = getValue(obj, arrayName);
        if (value != null) {
            return new URL(value);
        }
        return null;
    }

    protected TimeZone getTimeZone(JSONObject obj, String objName) throws JSONException {
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

    protected Integer getInt(JSONObject obj, String name) throws JSONException {
        if (obj.has(name)) {
            return obj.getInt(name);
        }
        return null;
    }

    protected String getString(JSONObject obj, String name) throws JSONException {
        if (obj.has(name)) {
            return obj.getString(name);
        }
        return null;
    }

    protected Float getFloat(JSONObject obj, String name) throws JSONException {
        if (obj.has(name)) {
            Double value = obj.getDouble(name);
            return value.floatValue();
        }
        return null;
    }
}

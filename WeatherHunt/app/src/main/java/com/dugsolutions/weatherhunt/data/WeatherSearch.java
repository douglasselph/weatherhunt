package com.dugsolutions.weatherhunt.data;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

import timber.log.Timber;

/**
 * Created by dug on 7/20/17.
 */

public class WeatherSearch extends WeatherBase {

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
            URL url = new URL(builder.build().toString());
            String result = post(url);
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

}

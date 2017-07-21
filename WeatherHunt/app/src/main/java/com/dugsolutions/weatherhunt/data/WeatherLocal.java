package com.dugsolutions.weatherhunt.data;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import timber.log.Timber;

/**
 * Created by dug on 7/20/17.
 */

public class WeatherLocal extends WeatherBase {

    static final Integer NUMBER_OF_DAYS = 5;

    public WeatherLocalResult query(String location) {
        WeatherLocalResult weatherResult = null;
        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(AUTHORITY)
                    .appendPath("premium")
                    .appendPath("v1")
                    .appendPath("weather.ashx")
                    .appendQueryParameter("q", location)
                    .appendQueryParameter("num_of_days", Integer.toString(NUMBER_OF_DAYS))
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("showlocaltime", "yes")
                    .appendQueryParameter("tp", "6")
                    .appendQueryParameter("key", KEY);
            URL url = new URL(builder.build().toString());
            String result = post(url);
            if (result == null) {
                return null;
            }
            weatherResult = new WeatherLocalResult();
            JSONObject object = new JSONObject(result);
            JSONObject data = object.getJSONObject("data");
            JSONArray request = data.getJSONArray("request");
            JSONObject requestEle = request.getJSONObject(0);
            weatherResult.requestType = requestEle.getString("type");
            weatherResult.requestLocation = requestEle.getString("query");
            JSONArray current_condition = data.getJSONArray("current_condition");
            if (current_condition.length() > 0) {
                JSONObject cc = current_condition.getJSONObject(0);
                weatherResult.conditionLocal = new ConditionLocal();
                query(weatherResult.conditionLocal, cc);
            }
            JSONArray weather = data.getJSONArray("weather");
            for (int i = 0; i < weather.length(); i++) {
                JSONObject instance = weather.getJSONObject(i);
                ConditionDate report = new ConditionDate();
                report.setDate(getString(instance, "date"));
                report.maxTempC = getInt(instance, "maxtempC");
                report.maxTempF = getInt(instance, "maxtempF");
                report.minTempC = getInt(instance, "mintempC");
                report.minTempF = getInt(instance, "mintempF");
                JSONArray hourlyArray = instance.getJSONArray("hourly");
                for (int h = 0; h < hourlyArray.length(); h++) {
                    JSONObject hourly = hourlyArray.getJSONObject(h);
                    ConditionHourly chour = new ConditionHourly();
                    query(chour, hourly);
                    chour.setTimeOffset(report.date, getString(hourly, "time"));
                    report.hourly.add(chour);
                }
                weatherResult.dates.add(report);
            }
        } catch (Exception ex) {
            Timber.e(ex);
        }
        return weatherResult;
    }

    void query(ConditionLocal local, JSONObject cc) throws JSONException, MalformedURLException {
        local.setTimeToday(getString(cc, "observation_time"));
        local.tempC = getInt(cc, "tempC");
        local.tempF = getInt(cc, "tempF");
        local.weatherCode = getInt(cc, "weatherCode");
        local.weatherIconUrl = getURL(cc, "weatherIconUrl");
        local.weatherDesc = getValue(cc, "weatherDesc");
        local.precipMM = getFloat(cc, "precipMM");

//        local.windspeedMiles = getInt(cc, "windspeedMiles");
//        local.windspeedKmph = getInt(cc, "windspeedKmph");
//        local.winddirDegree = getInt(cc, "winddirDegree");
//        local.winddir16Point = cc.getString("winddir16Point");
//        local.humidity = getInt(cc, "humidity");
//        local.visibility = getInt(cc, "visibility");
//        local.pressure = getInt(cc, "pressure");
//        local.cloudcover = getInt(cc, "cloudcover");
//        local.feelsLikeC = getInt(cc, "feelsLikeC");
//        local.feelsLikeF = getInt(cc, "feelsLikeF");
    }

}
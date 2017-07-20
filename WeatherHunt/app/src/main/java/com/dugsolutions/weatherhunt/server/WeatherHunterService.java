package com.dugsolutions.weatherhunt.server;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.dugsolutions.weatherhunt.event.WeatherResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by dug on 7/19/17.
 */

public class WeatherHunterService extends IntentService {

    public static final String SEARCH_LOCATION = "search";

    static final String SERVER_NAME = "WeatherHunterService";
    static final String KEY = "34c23c88e7314574828220153171907";
    static final String AUTHORITY = "api.worldweatheronline.com";

    public WeatherHunterService() {
        super(SERVER_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String search_locaton = intent.getExtras().getString(SEARCH_LOCATION);
        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(AUTHORITY)
                    .appendPath("premium")
                    .appendPath("v1")
                    .appendPath("search.ashx")
                    .appendQueryParameter("query", search_locaton)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("key", KEY);
            Uri uri = builder.build();
            URL url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream;
            inputStream = connection.getInputStream();
            if (inputStream == null) {
                Timber.e("NULL response from server");
                connection.disconnect();
                return;
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
            String result = sbuf.toString();
            EventBus.getDefault().post(new WeatherResult(result));

            inputStream.close();
            connection.disconnect();

            JSONObject object = new JSONObject(result);
            JSONObject root = object.getJSONObject("search_api");
            JSONArray elements = root.getJSONArray("result");
        } catch (Exception ex) {
            Timber.e(ex);
        }
    }
}

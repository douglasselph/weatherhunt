package com.dugsolutions.weatherhunt.server;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.dugsolutions.weatherhunt.event.WeatherResult;

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
    static final String SEARCH = "http://api.worldweatheronline.com/premium/v1/search.ashx";

    public WeatherHunterService() {
        super(SERVER_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String search_locaton = intent.getExtras().getString(SEARCH_LOCATION);
        try {
            JSONObject json = new JSONObject();
            json.accumulate("key", KEY);
            json.accumulate("format", "json");
            json.accumulate("query", search_locaton);
            String result = post(SEARCH, json);

            Timber.d("RESULT=" + result);
            EventBus.getDefault().post(new WeatherResult(result));

        } catch (Exception ex) {
            Timber.e(ex);
        }
    }

    String post(String target, JSONObject json) {
        try {
            URL url = new URL(target);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            Writer writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            Timber.d("MYDEBUG: REQUEST AWAY: " + json.toString());
            writer.write(json.toString());
            writer.close();
            InputStream inputStream;
            try {
                inputStream = connection.getInputStream();
            } catch (Exception ex) {
                Timber.e("Server response not available. (" + ex.getMessage() + ")");
                return null;
            }
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
            return sbuf.toString();
        } catch (Exception ex) {
            Timber.e(ex);
            return null;
        }
    }
}

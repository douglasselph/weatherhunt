package com.dugsolutions.weatherhunt;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.dugsolutions.weatherhunt.data.WeatherLocal;
import com.dugsolutions.weatherhunt.data.WeatherLocalResult;
import com.dugsolutions.weatherhunt.data.WeatherSearch;
import com.dugsolutions.weatherhunt.data.WeatherSearchResult;

import de.greenrobot.event.EventBus;

/**
 * Created by dug on 7/19/17.
 *
 * Query weather results from the internet from within a service. This is but one way to do it.
 * This way is nice because all internet work can occur quite independently from what activity
 * the user currently happens to be in.
 *
 * Another way would be to simply make the call within an async task. If the user leaves
 * the APP, there would be no reason to continue the query, and the async task could be killed.
 *
 * Another idea: if I wanted to demonstrate the use of RxJava, I could use that instead of this service.
 */
public class WeatherHunterService extends IntentService {

    public static final String SEARCH_LOCATION = "query";
    public static final String GET_WEATHER_LOCATION = "get_weather";

    static final String SERVER_NAME = "WeatherHunterService";

    public WeatherHunterService() {
        super(SERVER_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String location;
        location = intent.getExtras().getString(GET_WEATHER_LOCATION);
        if (location != null) {
            WeatherLocalResult result = new WeatherLocal().query(location);
            if (result != null) {
                EventBus.getDefault().post(result);
            }
        }
        location = intent.getExtras().getString(SEARCH_LOCATION);
        if (location != null) {
            WeatherSearchResult result = new WeatherSearch().query(location);
            if (result != null) {
                EventBus.getDefault().post(result);
            }
        }
    }
}

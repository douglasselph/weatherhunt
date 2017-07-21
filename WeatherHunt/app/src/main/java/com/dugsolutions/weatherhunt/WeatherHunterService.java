package com.dugsolutions.weatherhunt;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.dugsolutions.weatherhunt.data.WeatherLocal;
import com.dugsolutions.weatherhunt.data.WeatherLocalResult;
import com.dugsolutions.weatherhunt.data.WeatherSearch;
import com.dugsolutions.weatherhunt.data.WeatherSearchResult;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by dug on 7/19/17.
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

package com.dugsolutions.weatherhunt;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import timber.log.Timber;

/**
 * Created by dug on 7/19/17.
 */

public class MyApplication extends Application {
    ConnectivityManager mCM;

    @Override
    public void onCreate() {
        super.onCreate();
        mCM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public void huntForWeather(String loc) {
        if (hasConnection()) {
            Intent intent = new Intent(this, WeatherHunterService.class);
            intent.putExtra(WeatherHunterService.GET_WEATHER_LOCATION, loc);
            startService(intent);
        }
    }

    boolean hasConnection() {
        final NetworkInfo ni = mCM.getActiveNetworkInfo();
        if (ni != null) {
            if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }
}

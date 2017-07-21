package com.dugsolutions.weatherhunt.data;

import android.net.Uri;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by dug on 7/20/17.
 */

public class ConditionLocal {

    public static final SimpleDateFormat timeOnlyFormat = new SimpleDateFormat("HH:MM aa");
    public static final SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM dd HH:MM aa");

    static final char DEGREE = (char) 0x00B0;

    public long time;
    public Integer tempC;
    public Integer tempF;
    public Integer weatherCode;
    public URL weatherIconUrl;
    public String weatherDesc;
    public float precipMM;

    @Override
    public String toString() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(time);
        sbuf.append(" C=");
        sbuf.append(tempC);
        sbuf.append(" F=");
        sbuf.append(tempF);
        sbuf.append(" code=");
        sbuf.append(weatherCode);
        sbuf.append(" desc=");
        sbuf.append(weatherDesc);
        sbuf.append(" precip=");
        sbuf.append(precipMM);
        sbuf.append(" icon=");
        sbuf.append(weatherIconUrl.toString());
        return sbuf.toString();
    }

    public String getTimeString() {
        if (time > 0) {
            return dateTimeFormat.format(time);
        }
        return "ERROR";
    }

    public void setTimeToday(String timeToday) {
        if (timeToday != null) {
            try {
                String midnight = dateOnlyFormat.format(new Date(System.currentTimeMillis()));
                Date midnightDate = dateOnlyFormat.parse(midnight);
                Date timeOffset = timeOnlyFormat.parse(timeToday);
                time = midnightDate.getTime() + timeOffset.getTime();
            } catch (Exception ex) {
                Timber.e(ex);
            }
        }
    }

    public void setTimeOffset(long base, String timeNow) {
        if (timeNow != null) {
            float hours = Integer.parseInt(timeNow) / 100f;
            time = base + (int) (hours * 1000 * 60 * 60);
        }
    }

    public String getDesc() {
        return weatherDesc;
    }

    public String getTempString() {
        StringBuilder sbuf = new StringBuilder();
        if (tempC != null) {
            sbuf.append(tempC);
            sbuf.append("C");
            sbuf.append(DEGREE);
        }
        if (tempF != null) {
            if (sbuf.length() > 0) {
                sbuf.append("/");
            }
            sbuf.append(tempF);
            sbuf.append("F");
            sbuf.append(DEGREE);
        }
        return sbuf.toString();
    }

    public Uri getIconUri() {
        try {
            return Uri.parse(weatherIconUrl.toURI().toString());
        } catch (Exception ex) {
            Timber.e(ex);
        }
        return null;
    }
//    public Integer windspeedMiles;
//    public Integer windspeedKmph;
//    public Integer winddirDegree;
//    public String winddir16Point;
//    public Integer humidity;
//    public Integer visibility;
//    public Integer pressure;
//    public Integer cloudcover;
//    public Integer feelsLikeC;
//    public Integer feelsLikeF;

}

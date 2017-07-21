package com.dugsolutions.weatherhunt.data;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.dugsolutions.weatherhunt.R;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by dug on 7/20/17.
 */

public class ConditionLocal {

    public static final SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateOnlyFormat2 = new SimpleDateFormat("MMM dd");
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM dd hh:mm aa");
    public static final SimpleDateFormat dateTimeFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");

    static Drawable Raindrop;

    static final char DEGREE = (char) 0x00B0;

    public long time;
    public Integer tempC;
    public Integer tempF;
    public Integer weatherCode;
    public URL weatherIconUrl;
    public String weatherDesc;
    public float precipMM;
    boolean hasTimeOffset;

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

    String getTimeString() {
        if (time > 0) {
            if (hasTimeOffset) {
                return dateTimeFormat.format(time);
            }
            return dateOnlyFormat2.format(time);
        }
        return "ERROR";
    }

    public void setTimeToday(String timeToday) {
        if (timeToday != null) {
            try {
                StringBuilder sbuf = new StringBuilder();
                sbuf.append(dateOnlyFormat.format(new Date(System.currentTimeMillis())));
                sbuf.append(" ");
                sbuf.append(timeToday);
                time = dateTimeFormat2.parse(sbuf.toString()).getTime();
                hasTimeOffset = true;
            } catch (Exception ex) {
                Timber.e(ex);
            }
        }
    }

    public void setTimeOffset(long base, String timeNow) {
        if (timeNow != null) {
            float hours = Integer.parseInt(timeNow) / 100f;
            time = base + (int) (hours * 1000 * 60 * 60);
            hasTimeOffset = (hours > 0);
        }
    }

    String getDesc() {
        return weatherDesc;
    }

    String getTempString() {
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

    public CharSequence getLine() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(getTimeString());
        int endTime = sbuf.length();
        Integer raindropBegin = null;
        Integer raindropEnd = null;

        if (tempC != null || tempF != null) {
            sbuf.append("  ");
            sbuf.append(getTempString());
            if (precipMM > 0) {
                sbuf.append(" ");
                raindropBegin = sbuf.length();
                sbuf.append(" P ");
                raindropEnd = sbuf.length();
                sbuf.append(precipMM);
                sbuf.append(" mm");
            }
        }
        sbuf.append("\n");
        int beginDesc = sbuf.length();
        sbuf.append(getDesc());
        int endDesc = sbuf.length();

        SpannableString span = new SpannableString(sbuf.toString());
        span.setSpan(new StyleSpan(Typeface.BOLD), 0, endTime, 0);
        span.setSpan(new RelativeSizeSpan(1.1f), 0, endTime, 0);
        span.setSpan(new RelativeSizeSpan(1.1f), beginDesc, endDesc, 0);

        if (raindropBegin != null && Raindrop != null) {
            ImageSpan imageSpan = new ImageSpan(Raindrop, ImageSpan.ALIGN_BASELINE);
            span.setSpan(imageSpan, raindropBegin, raindropEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return span;
    }

    public static void InitRaindrop(Context ctx) {
        Raindrop = ResourcesCompat.getDrawable(ctx.getResources(), R.drawable.raindrop, null);
        Raindrop.setBounds(0, 0, Raindrop.getIntrinsicWidth(), Raindrop.getIntrinsicHeight());
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

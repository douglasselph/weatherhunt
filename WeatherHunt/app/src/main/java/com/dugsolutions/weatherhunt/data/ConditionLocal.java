package com.dugsolutions.weatherhunt.data;

import android.net.Uri;

import java.net.URL;

/**
 * Created by dug on 7/20/17.
 */

public class ConditionLocal {
    public String time;
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

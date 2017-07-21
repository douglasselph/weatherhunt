package com.dugsolutions.weatherhunt.data;

import java.util.ArrayList;

/**
 * Created by dug on 7/20/17.
 */

public class ConditionDate {
    public String date;
    public Integer maxTempC;
    public Integer maxTempF;
    public Integer minTempC;
    public Integer minTempF;

    public ArrayList<ConditionHourly> hourly = new ArrayList();

    @Override
    public String toString() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(date);
        sbuf.append(" tempC=");
        sbuf.append(minTempC);
        sbuf.append("->");
        sbuf.append(maxTempC);
        sbuf.append(" tempF=");
        sbuf.append(minTempF);
        sbuf.append("->");
        sbuf.append(maxTempF);
        sbuf.append("\n");
        for (ConditionHourly ele : hourly) {
            sbuf.append(ele.toString());
            sbuf.append("\n");
        }
        return sbuf.toString();
    }

    public int getCount() {
        return hourly.size();
    }
// Another available field: astronomy array
//    Integer totalSnow_cm;
//    float sunHour;
//    Integer uvIndex;
}

package com.dugsolutions.weatherhunt.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dug on 7/20/17.
 */

public class WeatherLocalResult {

    public String requestType;
    public String requestLocation;
    public ConditionLocal conditionLocal;
    public ArrayList<ConditionDate> dates = new ArrayList();

    public WeatherLocalResult() {
    }

    @Override
    public String toString() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append("City=");
        sbuf.append(requestType);
        sbuf.append("\n");
        sbuf.append("Query=");
        sbuf.append(requestLocation);
        sbuf.append("\n");
        sbuf.append(conditionLocal);
        sbuf.append("\n");
        for (ConditionDate ele : dates) {
            sbuf.append(ele.toString());
            sbuf.append("\n");
        }
        return sbuf.toString();
    }

    public int getCount() {
        int total = 1;
        for (ConditionDate date : dates) {
            total += date.getCount();
        }
        return total;
    }

    public ConditionLocal getCondition(int i) {
        if (i == 0) {
            return conditionLocal;
        }
        i--;
        for (ConditionDate date : dates) {
            if (i < date.getCount()) {
                return date.hourly.get(i);
            }
            i -= date.getCount();
        }
        return null;
    }
}

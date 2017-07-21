package com.dugsolutions.weatherhunt.data;

import java.net.URL;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by dug on 7/19/17.
 */

public class WeatherSearchResult {
    static class Location {
        public String areaName;
        public String country;
        public String region;
        public String latitude;
        public String longitude;
        public int population;
        public TimeZone timeZone;
        public URL url;

        @Override
        public String toString() {
            StringBuilder sbuf = new StringBuilder();
            add(sbuf, areaName);
            add(sbuf, country);
            add(sbuf, region);
            add(sbuf, latitude);
            add(sbuf, longitude);
            if (population > 0) {
                sbuf.append(",pop=");
                sbuf.append(population);
            }
            if (timeZone != null) {
                sbuf.append(",timezone=");
                sbuf.append(timeZone.getDisplayName());
            }
            if (url != null) {
                sbuf.append(",url=");
                sbuf.append(url.toString());
            }
            return sbuf.toString();
        }

        StringBuilder add(StringBuilder sbuf, String name) {
            if (name != null) {
                if (sbuf.length() > 0) {
                    sbuf.append(", ");
                }
                sbuf.append(name);
            }
            return sbuf;
        }
    }

    public ArrayList<Location> locations = new ArrayList();

    public void add(Location loc) {
        locations.add(loc);
    }

    @Override
    public String toString() {
        StringBuilder sbuf = new StringBuilder();
        for (int i = 0; i < locations.size(); i++) {
            if (i > 0) {
                sbuf.append("\n");
            }
            sbuf.append(locations.get(i).toString());
        }
        return sbuf.toString();
    }
}

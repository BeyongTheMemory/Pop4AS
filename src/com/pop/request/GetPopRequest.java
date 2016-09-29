package com.pop.request;

/**
 * Created by xugang on 16/9/28.
 */
public class GetPopRequest {
    private double lat;
    private double lon;


    public GetPopRequest(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }

}
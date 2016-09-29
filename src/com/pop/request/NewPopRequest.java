package com.pop.request;

/**
 * Created by xugang on 16/9/28.
 */
public class NewPopRequest {
    private int type;
    private double longitude;
    private double latitude;
    private double altitude;
    private int model;
    private String imgUrl;
    private String message;
    private int isShowy;
    public int onlyOnce;
    private int anonymous;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIsShowy() {
        return isShowy;
    }

    public void setIsShowy(int isShowy) {
        this.isShowy = isShowy;
    }


    public int getOnlyOnce() {
        return onlyOnce;
    }

    public void setOnlyOnce(int onlyOnce) {
        this.onlyOnce = onlyOnce;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }
}
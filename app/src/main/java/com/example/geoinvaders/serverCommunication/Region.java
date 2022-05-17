package com.example.geoinvaders.serverCommunication;

public class Region {
    private String username;
    private double latitude;
    private double longitude;
    private String rgb;

    public Region(String username, double latitude, double longitude, String rgb) {
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rgb = rgb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }
}

package com.example.nearbylocaton.pogos;

import com.example.nearbylocaton.models.Photos;

public class Favorite {
    private String placename;
    private double latitute;
    private double longitude;
    private String rating;
    private String time;
    private String iconURL;
    private Photos[] photos;

    public String getPlacename() {
        return placename;
    }

    public Photos[] getPhotos() {
        return photos;
    }

    public void setPhotos(Photos[] photos) {
        this.photos = photos;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public double getLatitute() {
        return latitute;
    }

    public void setLatitute(double latitute) {
        this.latitute = latitute;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String icon) {
        this.iconURL = icon;
    }

    public Favorite(String placename, double latitute, double longitude, String rating, String time, String icon) {
        this.placename = placename;
        this.latitute = latitute;
        this.longitude = longitude;
        this.rating = rating;
        this.time = time;
        this.iconURL = icon;
    }
}

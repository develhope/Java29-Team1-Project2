package com.develhope.maps.models.components;

public class Location {
    private LatLng latLng;

    public Location(Double latitude, Double longitude) {
        this.latLng = new LatLng(latitude, longitude);
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}

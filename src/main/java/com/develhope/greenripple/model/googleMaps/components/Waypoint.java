package com.develhope.greenripple.model.googleMaps.components;

public class Waypoint {
    private Location location;

    public Waypoint(Double latitude, Double longitude) {
        this.location = new Location(latitude, longitude);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

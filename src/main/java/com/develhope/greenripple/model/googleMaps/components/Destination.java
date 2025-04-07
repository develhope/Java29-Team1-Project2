package com.develhope.greenripple.model.googleMaps.components;

public class Destination {
    private Waypoint waypoint;

    public Destination(Double latitude, Double longitude) {
        this.waypoint = new Waypoint(latitude, longitude);
    }

    public Waypoint getWaypoint() {
        return waypoint;
    }

    public void setWaypoint(Waypoint waypoint) {
        this.waypoint = waypoint;
    }
}

package com.develhope.greenripple.model.googleMaps.components;

public class Origin {
    private Waypoint waypoint;
    private RouteModifiers routeModifiers;

    public Origin(Double latitude, Double longitude) {
        this.waypoint = new Waypoint(latitude, longitude);
        this.routeModifiers = new RouteModifiers(true);
    }

    public Waypoint getWaypoint() {
        return waypoint;
    }

    public void setWaypoint(Waypoint waypoint) {
        this.waypoint = waypoint;
    }

    public RouteModifiers getRouteModifiers() {
        return routeModifiers;
    }

    public void setRouteModifiers(RouteModifiers routeModifiers) {
        this.routeModifiers = routeModifiers;
    }
}

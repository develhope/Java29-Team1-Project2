package com.develhope.maps.models.components;

public class RouteModifiers {
    private Boolean avoidFerries;

    public RouteModifiers(Boolean avoidFerries) {
        this.avoidFerries = avoidFerries;
    }

    public Boolean getAvoidFerries() {
        return avoidFerries;
    }

    public void setAvoidFerries(Boolean avoidFerries) {
        this.avoidFerries = avoidFerries;
    }
}

package com.develhope.maps.models;

import com.develhope.maps.models.components.Destination;
import com.develhope.maps.models.components.Origin;

import java.util.List;

public class GoogleMapsInputMatrix {
    private List<Origin> origins;
    private List<Destination> destinations;
    private String travelMode;
    private String routingPreference;

    public GoogleMapsInputMatrix(List<Origin> origins, List<Destination> destinations) {
        this.origins = origins;
        this.destinations = destinations;
        this.travelMode = "DRIVE";
        this.routingPreference = "TRAFFIC_AWARE";
    }

    public List<Origin> getOrigins() {
        return origins;
    }

    public void setOrigins(List<Origin> origins) {
        this.origins = origins;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getRoutingPreference() {
        return routingPreference;
    }

    public void setRoutingPreference(String routingPreference) {
        this.routingPreference = routingPreference;
    }
}

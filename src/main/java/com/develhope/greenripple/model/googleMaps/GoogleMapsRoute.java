package com.develhope.greenripple.model.googleMaps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"km", "carCO2"})
public class GoogleMapsRoute {
    private Long distanceMeters;

    @JsonCreator
    public GoogleMapsRoute(
            @JsonProperty("distanceMeters") Long distanceMeters) {

        this.distanceMeters = distanceMeters;
    }

    @JsonIgnore
    public Long getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(Long distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public Double getKm() {
        return (double) distanceMeters / 1000;
    }

    // Grams of CO2 emitted by a car
    public Double getCarCO2Grams() {
        return distanceMeters * 0.165;
    }

}

package com.develhope.greenripple.enumerations;

public enum CarType {
    GASOLINE("Gasoline", 165.0), // in grams of CO2 per km
    DIESEL("Diesel", 140.0),
    HYBRID("Hybrid", 100.0), // Average between gasoline and electric
    ELECTRIC("Electric", 0.0), // No CO2 emissions
    LPG("LPG", 130.0);

    private final String fuelType;
    private final Double co2PerKm; // in grams per km

    // Enum constructor
    CarType(String fuelType, Double co2PerKm) {
        this.fuelType = fuelType;
        this.co2PerKm = co2PerKm;
    }

    // Getters
    public String getFuelType() {
        return fuelType;
    }

    public Double getCo2PerKm() {
        return co2PerKm;
    }
}

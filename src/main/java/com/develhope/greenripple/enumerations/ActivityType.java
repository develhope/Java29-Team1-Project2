package com.develhope.greenripple.enumerations;

public enum ActivityType {
    PUBLIC_TRANSPORT("Use of public transports (bus, train, exc.)"),
    ELECTRIC_TRANSPORT("Use of electric transports (electric car, e-scooter, exc.)"),
    MECHANICAL_TRANSPORT("Use of mechanical transports or walk (bike, skateboard, exc.)"),
    WASTE_SORTING("Use of public recycling bins"),
    PASSIVE_ENERGY_GENERATORS("Use of devices which produce energy as a consequence of their use"),
    ACTIVE_ENERGY_GENERATORS("Use of devices which purpose is just to generate energy");

    private final String description;

    ActivityType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

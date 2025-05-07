package com.develhope.greenripple.entities;

import com.develhope.greenripple.enumerations.ActivityType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "activities")
public class Activity {

    private static final Double GREEN_POINTS_PER_CO2_GRAM = 0.01;
    private static final Double GREEN_POINTS_PER_ENERGY_JOULE = 0.001;
    private static final Double VOTES_PER_CO2_GRAM = 0.001;
    private static final Double VOTES_PER_ENERGY_JOULE = 0.0001;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // Date and time when the activity was performed.
    @Column(name = "date")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Europe/Rome")
    private OffsetDateTime date = OffsetDateTime.now();

    // Amount of energy produced by the activity.
    @Column(name = "produced_energy_joules")
    private Double producedEnergyJoules = 0.0;

    @Column(name = "saved_CO2_grams")
    private Double savedCO2Grams = 0.0;

    @Column(name = "activity_type")
    @Enumerated(value = EnumType.STRING)
    private ActivityType activityType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public Activity() {
    }

    public Activity(Long id, String name, OffsetDateTime date, Double producedEnergyJoules, Double savedCO2Grams, ActivityType activityType, User user) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.producedEnergyJoules = producedEnergyJoules;
        this.savedCO2Grams = savedCO2Grams;
        this.activityType = activityType;
        this.user = user;
    }


    // Methods
    public Double gainedGreenPoints() {
        return savedCO2Grams * GREEN_POINTS_PER_CO2_GRAM + producedEnergyJoules * GREEN_POINTS_PER_ENERGY_JOULE;
    }

    public Double gainedVotes() {
        return savedCO2Grams * VOTES_PER_CO2_GRAM + producedEnergyJoules * VOTES_PER_ENERGY_JOULE;
    }


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Double getProducedEnergyJoules() {
        return producedEnergyJoules;
    }

    public void setProducedEnergyJoules(Double producedEnergyJoules) {
        this.producedEnergyJoules = producedEnergyJoules;
    }

    public Double getSavedCO2Grams() {
        return savedCO2Grams;
    }

    public void setSavedCO2Grams(Double savedCO2Grams) {
        this.savedCO2Grams = savedCO2Grams;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

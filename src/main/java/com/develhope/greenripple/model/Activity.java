package com.develhope.greenripple.model;

import com.develhope.greenripple.enumerations.ActivityType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    //Date and time when the activity was performed.
    @Column(name = "date")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Europe/Rome")
    private OffsetDateTime date = OffsetDateTime.now();

    //Amount of energy produced by the activity.
    @Column(name = "produced_energy")
    private Double producedEnergy;

    @Column(name = "saved_CO2_grams")
    private Double savedCO2Grams;

    @Column(name = "activity_type")
    @Enumerated(value = EnumType.STRING)
    private ActivityType activityType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Constructors
    public Activity() {
    }

    public Activity(Long id, String name, OffsetDateTime date, Double producedEnergy, Double savedCO2, ActivityType activityType, User user) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.producedEnergy = producedEnergy;
        this.savedCO2Grams = savedCO2;
        this.activityType = activityType;
        this.user = user;
    }

    //Getters and Setters
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

    public Double getProducedEnergy() {
        return producedEnergy;
    }

    public void setProducedEnergy(Double producedEnergy) {
        this.producedEnergy = producedEnergy;
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

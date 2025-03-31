package com.develhope.greenripple.model;

import com.develhope.greenripple.enumerations.ActivityType;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    private OffsetDateTime date;

    @Column(name = "produced_energy")
    private Long producedEnergy;

    @Column(name = "saved_CO2")
    private Long savedCO2;

    @Column(name = "activity_type")
    @Enumerated(value = EnumType.STRING)
    private ActivityType activityType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Activity() {
    }

    public Activity(Long id, String name, OffsetDateTime date, Long producedEnergy, Long savedCO2, ActivityType activityType, User user) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.producedEnergy = producedEnergy;
        this.savedCO2 = savedCO2;
        this.activityType = activityType;
        this.user = user;
    }

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

    public Long getProducedEnergy() {
        return producedEnergy;
    }

    public void setProducedEnergy(Long producedEnergy) {
        this.producedEnergy = producedEnergy;
    }

    public Long getSavedCO2() {
        return savedCO2;
    }

    public void setSavedCO2(Long savedCO2) {
        this.savedCO2 = savedCO2;
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

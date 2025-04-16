package com.develhope.greenripple.dto;

import com.develhope.greenripple.enumerations.ActivityType;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityDTO {

    private Long id;
    private String name;
    private String date;
    private Long producedEnergy;
    private Long savedCO2;
    private ActivityType activityType;
    private Long userId;
    private String userName;

    public ActivityDTO(Long id, String name, OffsetDateTime date, Long producedEnergy, Long savedCO2, ActivityType activityType, Long userId, String userName) {
        this.id = id;
        this.name = name;
        this.date = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        this.producedEnergy = producedEnergy;
        this.savedCO2 = savedCO2;
        this.activityType = activityType;
        this.userId = userId;
        this.userName = userName;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

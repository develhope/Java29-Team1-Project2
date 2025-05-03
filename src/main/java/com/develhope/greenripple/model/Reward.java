package com.develhope.greenripple.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotNull(message = "Reward name cannot be null")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "required_green_points", nullable = false)
    private Integer requiredGreenPoints;

    // One-to-Many relationship: A reward can be redeemed multiple times by different users
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "redeemed_by")
    private User redeemedBy;

    // Constructors
    public Reward() {}

    public Reward(Long id, String name, String description, int requiredGreenPoints) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.requiredGreenPoints = requiredGreenPoints;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRequiredGreenPoints() {
        return requiredGreenPoints;
    }

    public void setRequiredGreenPoints(Integer requiredGreenPoints) {
        this.requiredGreenPoints = requiredGreenPoints;
    }

}

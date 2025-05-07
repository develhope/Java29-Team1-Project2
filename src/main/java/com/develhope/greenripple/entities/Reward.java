package com.develhope.greenripple.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Reward name cannot be blank")
    private String name;

    @Column(name = "description")
    private String description;

    @Min(value = 1, message = "Required green points must be at least 1")
    @Column(name = "required_green_points", nullable = false)
    private Integer requiredGreenPoints;

    // One-to-Many relationship: A reward can be redeemed multiple times by different users
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

    public User getRedeemedBy() {
        return redeemedBy;
    }

    public void setRedeemedBy(User redeemedBy) {
        this.redeemedBy = redeemedBy;
    }
}

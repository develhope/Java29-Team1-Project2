package com.develhope.greenripple.entities;

import com.develhope.greenripple.enumerations.CarType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Entity representing a User
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(min = 2, max = 100)
    @NotNull(message = "Name cannot be null")
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    @Size(min = 8, max = 255)
    @Email(message = "Email should be valid")
    @NotNull(message = "Email cannot be null")
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Column(name = "city")
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "car_type")
    private CarType carType;

    @Column(name = "green_points")
    private Double greenPoints = 0.0;

    @Column(name = "votes")
    private Double votes = 0.0;

    // Add a field to indicate if the user is logically deleted
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;  // Default is false (not deleted)

   // One-to-Many relationship: A user can redeem multiple rewards
    @JsonIgnore
    @OneToMany(mappedBy = "redeemedBy", cascade = CascadeType.ALL)
    private List<Reward> redeemedRewards;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Activity> activities;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> votedProjects;

    // Constructors
    public User() {}

    public User(Long id, String name, String email, String password, String city, Double greenPoints, Double votes, CarType carType, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.city = city;
        this.carType = carType;
        this.greenPoints = greenPoints;
        this.votes = votes;
        this.isDeleted = isDeleted;
    }

    public User(String name, String email, String password, String city, CarType carType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.city = city;
        this.carType = carType;
    }

    // Methods
    public void increaseGreenPoints(Double greenPoints) {
        this.greenPoints += greenPoints;
    }

    public void increaseVotes(Double votes) {
        this.votes += votes;
    }

    // Implemented methods from UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getGreenPoints() {
        return greenPoints;
    }

    public void setGreenPoints(Double greenPoints) {
        this.greenPoints = greenPoints;
    }

    public Double getVotes() {
        return votes;
    }

    public void setVotes(Double votes) {
        this.votes = votes;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<UserProject> getVotedProjects() {
        return votedProjects;
    }

    public void setVotedProjects(List<UserProject> votedProjects) {
        this.votedProjects = votedProjects;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Reward> getRedeemedRewards() {
        return redeemedRewards;
    }

    public void setRedeemedRewards(List<Reward> redeemedRewards) {
        this.redeemedRewards = redeemedRewards;
    }

}

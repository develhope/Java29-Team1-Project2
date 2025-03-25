package com.develhope.greenripple.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

// Entity representing a User
@Entity
@Table(name = "users")
public class User {

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

    @Column(name = "green_points")
    private int greenPoints = 0;

    @Column(name = "votes")
    private int votes = 0;

    // Add a field to indicate if the user is logically deleted
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;  // Default is false (not deleted)

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Activity> activities;

    // Constructors
    public User() {}

    public User(String name, String email, String password, String city) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.city = city;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGreenPoints() {
        return greenPoints;
    }

    public void setGreenPoints(int greenPoints) {
        this.greenPoints = greenPoints;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

package com.develhope.greenripple.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "required_votes")
    private Long requiredVotes;

    @Column(name = "received_votes")
    private Long receivedVotes = 0L;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> votedByUsers;

    public Project() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRequiredVotes() {
        return requiredVotes;
    }

    public void setRequiredVotes(Long requiredVotes) {
        this.requiredVotes = requiredVotes;
    }

    public Long getReceivedVotes() {
        return receivedVotes;
    }

    public void setReceivedVotes(Long receivedVotes) {
        this.receivedVotes = receivedVotes;
    }

    public List<UserProject> getVotedByUsers() {
        return votedByUsers;
    }

    public void setVotedByUsers(List<UserProject> votedByUsers) {
        this.votedByUsers = votedByUsers;
    }
}

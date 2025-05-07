package com.develhope.greenripple.services;

import com.develhope.greenripple.entities.*;
import com.develhope.greenripple.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProjectService {

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Votes a project for a user.
     * @param userId The ID of the user attempting to vote the project.
     * @param projectId The ID of the project to be voted.
     * @return An optional containing the voted project details or empty if conditions are not met.
     * @throws RuntimeException If the user or project is not found, or if voting conditions are not met.
     */
    public Optional<UserProject> voteProject(Long userId, Long projectId) throws RuntimeException {
        // Retrieve the user and project from the database
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Project> projectOptional = projectRepository.findById(projectId);

        // Check if the user exists
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // Check if the project exists
        if (projectOptional.isEmpty()) {
            throw new RuntimeException("Project not found with ID: " + projectId);
        }

        // Extract user and reward objects
        User user = userOptional.get();
        Project project = projectOptional.get();

        // Check if the user has votes left
        if (user.getVotes() < 1) {
            throw new RuntimeException("No votes left!");
        }

        // Check if project can be voted
        if (project.getReceivedVotes() >= project.getRequiredVotes()) {
            throw new RuntimeException("Project just reached required amount of votes");
        }

        // Deduct user votes by 1
        user.setVotes(user.getVotes() - 1);

        // Increase received votes of the project by 1
        project.setReceivedVotes(project.getReceivedVotes() + 1);

        // Create a new UserProject entry
        UserProject userProject = userProjectRepository.save(new UserProject(user, project));

        // Save the updated user and project entities to the database
        userRepository.save(user);
        projectRepository.save(project);

        // Return the saved vote
        return Optional.of(userProject);
    }

    /**
     * Retrieves all projects voted by a specific user.
     * @param userId The ID of the user whose voted projects are being retrieved.
     * @return A list of all projects voted by the user.
     */
    public List<UserProject> getUserProjects(Long userId) {
        return userProjectRepository.findByUserIdOrderByVotedAtDesc(userId);
    }
}

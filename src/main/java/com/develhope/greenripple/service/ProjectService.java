package com.develhope.greenripple.service;

import com.develhope.greenripple.model.Project;
import com.develhope.greenripple.model.Reward;
import com.develhope.greenripple.repository.ProjectRepository;
import com.develhope.greenripple.repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Create a new Project and save it to the database.
     * @param project The project to be created.
     * @return The saved project.
     */
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    /**
     * Retrieve all projects stored in the database.
     * @return A list of all available projects.
     */
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Find a project by its unique ID.
     * @param id The ID of the project.
     * @return An optional containing the found project, or empty if not found.
     */
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    /**
     * Update an existing project with new details.
     * @param id The ID of the project to be updated.
     * @param updatedProject The new details to update the project with.
     * @return An optional containing the updated project, or empty if not found.
     */
    public Optional<Project> updateProject(Long id, Project updatedProject) {
        Optional<Project> existingProject = projectRepository.findById(id);

        // If the reward exists, update its fields
        if (existingProject.isPresent()) {

            existingProject.get().setName(updatedProject.getName());
            existingProject.get().setDescription(updatedProject.getDescription());
            existingProject.get().setRequiredVotes(updatedProject.getRequiredVotes());
            existingProject.get().setReceivedVotes(updatedProject.getReceivedVotes());

            // Save the updated reward
            Project savedProject = projectRepository.save(existingProject.get());
            return Optional.of(savedProject);
        }
        return Optional.empty();  // Return empty if the reward is not found
    }

    /**
     * Delete a project by its ID.
     * @param id The ID of the project to be deleted.
     */
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}

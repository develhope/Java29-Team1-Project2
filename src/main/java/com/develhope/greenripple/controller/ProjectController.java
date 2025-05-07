package com.develhope.greenripple.controller;

import com.develhope.greenripple.model.Project;
import com.develhope.greenripple.model.Reward;
import com.develhope.greenripple.service.ProjectService;
import com.develhope.greenripple.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // Create a new project
    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    // Get all projects
    @GetMapping("/get-all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    // Get project by ID
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);

        if (project.isPresent()) {
            return ResponseEntity.ok(project.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Project with ID '" + id + "' not found.");
    }

    // Update an existing project
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
        Optional<Project> optionalProject = projectService.updateProject(id, updatedProject);

        if (optionalProject.isPresent()) {
            return ResponseEntity.ok(optionalProject.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Project with ID '" + id + "' not found.");
    }

    // Delete a project by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);

        if (project.isPresent()) {
            projectService.deleteProject(id);
            return ResponseEntity.ok("Project with ID '" + id + "' deleted successfully.");
        }

        // Restituire un messaggio personalizzato se il progetto non viene trovato
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Project with ID '" + id + "' not found.");
    }
}

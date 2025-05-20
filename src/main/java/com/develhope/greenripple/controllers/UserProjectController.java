package com.develhope.greenripple.controllers;

import com.develhope.auth.services.AuthService;
import com.develhope.greenripple.entities.User;
import com.develhope.greenripple.entities.UserProject;
import com.develhope.greenripple.services.UserProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/user-projects")
public class UserProjectController {

    @Autowired
    private UserProjectService userProjectService;

    @Autowired
    private AuthService authService;

    // Endpoint to vote a projects for a user
    @PostMapping("/vote")
    public ResponseEntity<?> voteProject(@RequestParam Long projectId) {
        try {
            // Attempt to vote the project
            User currentUser = authService.currentUser();
            Optional<UserProject> userProject = userProjectService.voteProject(currentUser.getId(), projectId);

            if (userProject.isPresent()) {
                // Return the voted project with HTTP 200 (OK)
                return ResponseEntity.ok(userProject.get());
            }

            return ResponseEntity.notFound().build();

        } catch (RuntimeException e) {
            // If an error occurs, return HTTP 400 (Bad Request)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // Endpoint to retrieve all projects voted by a specific user
    @GetMapping("")
    public ResponseEntity<List<UserProject>> getUserProjects() {
        User currentUser = authService.currentUser();
        List<UserProject> userProjects = userProjectService.getUserProjects(currentUser.getId());

        if (userProjects.isEmpty()) {
            // If no projects were found for the user, return a 204 No Content status
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // If projects are found, return them with a 200 OK status
        return new ResponseEntity<>(userProjects, HttpStatus.OK);
    }
}

package com.greenripple.green_ripple.controller;

import com.greenripple.green_ripple.exceptions.UserNotFoundException;
import com.greenripple.green_ripple.model.User;
import com.greenripple.green_ripple.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Create a new user (registration)
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Get all users, excluding logically deleted users
    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by email (for login purposes)
    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Get user by ID (profile retrieval)
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Update an existing user profile
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = userService.updateUser(id, updatedUser);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Increase the green points for a user
    @PutMapping("/increase-green-points/{id}")
    public ResponseEntity<User> increaseGreenPoints(@PathVariable Long id, @RequestParam int points) {
        Optional<User> updatedUser = userService.increaseGreenPoints(id, points);

        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Decrease the green points for a user
    @PutMapping("/decrease-green-points/{id}")
    public ResponseEntity<Optional<User>> decreasePoints(@PathVariable Long id, @RequestParam int points) {
        try {
            Optional<User> updatedUser = userService.decreaseGreenPoints(id, points);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    // Login user with simplified Base64 authentication
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        try {
            Map<String, String> tokenResponse = userService.login(email, password);
            return ResponseEntity.ok(tokenResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }
    }

    // Soft delete a user (set isDeleted to true)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            return ResponseEntity.ok("User deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    // Restore a deleted user (set isDeleted to false)
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreUser(@PathVariable Long id) {
        boolean restored = userService.restoreUser(id);

        if (restored) {
            return ResponseEntity.ok("User restored successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}

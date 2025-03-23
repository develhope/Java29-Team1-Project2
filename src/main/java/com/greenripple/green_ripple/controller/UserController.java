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
        try {
            Optional<User> user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user.get()); // Se utente trovato
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Se eccezione sollevata
        }
    }

    // Get user by ID (profile retrieval)
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.getUserById(id);
            return ResponseEntity.ok(user.get()); // Se utente trovato
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Se eccezione sollevata
        }
    }

    // Update an existing user profile
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            Optional<User> optionalUser = userService.updateUser(id, updatedUser);  // Chiamata al service

            if (optionalUser.isPresent()) {
                return ResponseEntity.ok(optionalUser.get());  // Se l'utente esiste, ritorna il risultato
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Se utente non trovato
            }
        } catch (UserNotFoundException e) {
            // (return 404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Update the green points for a user
    @PutMapping("/update-green-points/{id}")
    public ResponseEntity<User> updateGreenPoints(@PathVariable Long id, @RequestParam int points) {
        try {
            User updatedUser = userService.updateGreenPoints(id, points);
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
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // Restore a deleted user (set isDeleted to false)
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreUser(@PathVariable Long id) {
        try {
            userService.restoreUser(id);
            return ResponseEntity.ok("User restored successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}

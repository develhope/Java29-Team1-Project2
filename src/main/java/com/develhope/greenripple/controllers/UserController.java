package com.develhope.greenripple.controllers;

import com.develhope.auth.services.AuthService;
import com.develhope.greenripple.entities.Activity;
import com.develhope.greenripple.entities.User;
import com.develhope.greenripple.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    // Get all users, excluding logically deleted users
    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by email (for login purposes)
    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        // Return a NOT_FOUND with a custom message
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User with email '" + email + "' not found.");
    }

    // Get user by ID (profile retrieval)
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        // Return NOT_FOUND with custom message
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User with ID '" + id + "' not found.");
    }

    // Update an existing user profile
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = userService.updateUser(id, updatedUser);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }

        // Return NOT_FOUND with custom message
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User with ID '" + id + "' not found for update.");
    }


    // Update a user's car type
    @PutMapping("/update-car-type/{id}")
    public ResponseEntity<?> updateCarType(@PathVariable Long id, @RequestBody User userWithNewCarType) {
        // Check that carType is not null
        if (userWithNewCarType.getCarType() == null) {
            // Return an error with a message in the response body
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "The car type (carType) cannot be null.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // Call the service to update the car type
        Optional<User> optionalUser = userService.updateCarType(id, userWithNewCarType.getCarType());

        // If the user exists and the update was successful
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }

        // NOT_FOUND with custom message
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User with ID '" + id + "' not found for car type update.");
    }

//    // Increase the green points for a user
//    @PutMapping("/increase-green-points/{id}")
//    public ResponseEntity<User> increaseGreenPoints(@PathVariable Long id, @RequestParam int points) {
//        Optional<User> updatedUser = userService.increaseGreenPoints(id, points);
//
//        if (updatedUser.isPresent()) {
//            return ResponseEntity.ok(updatedUser.get());
//        }
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//
//    // Decrease the green points for a user
//    @PutMapping("/decrease-green-points/{id}")
//    public ResponseEntity<Optional<User>> decreasePoints(@PathVariable Long id, @RequestParam int points) {
//        try {
//            Optional<User> updatedUser = userService.decreaseGreenPoints(id, points);
//            return ResponseEntity.ok(updatedUser);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

    // Soft delete a user (set isDeleted to true)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);  // Verifica se l'utente esiste

        if (user.isEmpty()) {
            // Se l'utente non esiste, restituisci un errore con il messaggio "User not found"
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID '" + id + "' not found.");
        }

        // Se l'utente esiste, procedi con l'eliminazione
        userService.deleteUser(id);

        // Restituisci una risposta di successo
        return ResponseEntity.status(HttpStatus.OK)
                .body("User deleted successfully");
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

    // Endpoint to retrieve the activity history of a user
    @GetMapping("/activity-history")
    public ResponseEntity<?> getUserActivityHistory() {
        User currentUser = authService.currentUser();
        Optional<User> user = userService.getUserById(currentUser.getId());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID '" + currentUser.getId() + "' not found.");
        }

        List<Activity> activities = userService.getUserActivityHistory(currentUser.getId());
        if (!activities.isEmpty()) {
            return ResponseEntity.ok(activities);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No activity history found for user ID '" + currentUser.getId() + "'.");
    }

    // Endpoint to retrieve the dashboard metrics of a user
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboardMetrics() {

        User currentUser = authService.currentUser();
        Map<String, Long> metrics = userService.getDashboardMetrics(currentUser.getId());

        // Build the response with numeric values (no units)
        Map<String, Long> formattedMetrics = new HashMap<>();
        formattedMetrics.put("TotalCO2GramsSaved", metrics.get("TotalCO2Saved"));
        formattedMetrics.put("TotalJoulesProduced", metrics.get("TotalEnergyProduced"));

        return ResponseEntity.ok(formattedMetrics);
    }


    // Get the current authenticated user
    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {

        User currentUser = authService.currentUser();
        return ResponseEntity.ok(currentUser);

    }

}

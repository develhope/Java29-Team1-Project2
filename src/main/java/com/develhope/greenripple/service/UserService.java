package com.develhope.greenripple.service;

import com.develhope.greenripple.enumerations.CarType;
import com.develhope.greenripple.model.Activity;
import com.develhope.greenripple.model.User;
import com.develhope.greenripple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

// Service class for handling User business logic
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityService activityService;

    /**
     * Creates a new user (registration).
     * @param user The user object to be created.
     * @return The saved user object.
     */
    /**
     * Creates a new user (registration).
     * If the email already exists, it returns an error response.
     *
     * @param user The user object to be created.
     * @return The saved user object or an error response if the email is already taken.
     */
    public ResponseEntity<User> createUser(User user) {
        // Check if a user with the same email already exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            // If the email already exists, return a 400 BAD REQUEST with a message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        // If email does not exist, save the new user
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);  // 201 CREATED status
    }

    /**
     * Retrieves all users, excluding those that have been logically deleted.
     * @return A list of active users.
     */
    public List<User> getAllUsers() {
        // Method to get only non-deleted users
        return userRepository.findByIsDeletedFalse();
    }

    /**
     * Retrieves a user by email (used for login purposes).
     * @param email The email of the user.
     * @return An optional containing the user if found.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email); // Modified to avoid exception
    }

    /**
     * Retrieves a user by ID (for profile lookup).
     * @param id The ID of the user.
     * @return An optional containing the user if found.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Updates an existing user's profile.
     * @param id The ID of the user to be updated.
     * @param updatedUser The updated user information.
     * @return An optional containing the updated user if found.
     */
    public Optional<User> updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            existingUser.get().setName(updatedUser.getName());
            existingUser.get().setEmail(updatedUser.getEmail());
            existingUser.get().setPassword(updatedUser.getPassword());
            existingUser.get().setCity(updatedUser.getCity());
            existingUser.get().setCarType(updatedUser.getCarType());

            User userSaved = userRepository.save(existingUser.get());
            return Optional.of(userSaved);
        }
        return Optional.empty();
    }

    /**
     * Updates the car type of a user and grants green points as a bonus
     * depending on the type of transition (e.g., from Diesel to Electric).
     *
     * @param userId  The ID of the user to update.
     * @param carType The new car type to set.
     * @return An Optional containing the updated user if found, otherwise Optional.empty().
     */
    public Optional<User> updateCarType(Long userId, CarType carType) {
        Optional<User> existingUser = userRepository.findById(userId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            CarType previousCarType = user.getCarType();  // Store previous car type

            // Initialize bonus points
            int bonusPoints = 0;

            // Check if the car type has changed
            if (previousCarType != carType) {
                // Assign bonus points based on the type of transition
                if (previousCarType == CarType.DIESEL && carType == CarType.ELECTRIC) {
                    bonusPoints = 200; // High bonus for switching from Diesel to Electric
                } else if (previousCarType == CarType.DIESEL && carType == CarType.HYBRID) {
                    bonusPoints = 150; // Medium bonus for switching from Diesel to Hybrid
                } else if (previousCarType == CarType.GASOLINE && carType == CarType.HYBRID) {
                    bonusPoints = 100; // Small bonus for switching from Gasoline to Hybrid
                } else if (previousCarType == CarType.GASOLINE && carType == CarType.ELECTRIC) {
                    bonusPoints = 250; // High bonus for switching from Gasoline to Electric
                } else if (previousCarType == CarType.GASOLINE && carType == CarType.LPG) {
                    bonusPoints = 120; // Bonus for switching from Gasoline to LPG
                } else if (previousCarType == CarType.DIESEL && carType == CarType.LPG) {
                    bonusPoints = 130; // Bonus for switching from Diesel to LPG
                } else if (previousCarType == CarType.LPG && carType == CarType.HYBRID) {
                    bonusPoints = 100; // Bonus for switching from LPG to Hybrid
                } else if (previousCarType == CarType.LPG && carType == CarType.ELECTRIC) {
                    bonusPoints = 150; // High bonus for switching from LPG to Electric
                }
            }

            // Update car type
            user.setCarType(carType);

            // Add bonus points to user's greenPoints
            user.setGreenPoints(user.getGreenPoints() + bonusPoints);

            // Save updated user
            User savedUser = userRepository.save(user);
            return Optional.of(savedUser);
        }

        return Optional.empty(); // User not found
    }


    /**
     * Increases a user's green points.
     * @param id The ID of the user.
     * @param points The number of green points to add.
     * @return An optional containing the updated user if found.
     */
    public Optional<User> increaseGreenPoints(Long id, int points) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Increment green points
            user.setGreenPoints(user.getGreenPoints() + points);

            userRepository.save(user);

            return Optional.of(user);
        }
        return Optional.empty();
    }

    /**
     * Decreases a user's green points.
     * @param id The ID of the user.
     * @param points The number of green points to subtract.
     * @return An optional containing the updated user if found.
     */
    public Optional<User> decreaseGreenPoints(Long id, int points) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setGreenPoints(user.getGreenPoints() - points);  // Decrease green points
            userRepository.save(user);  // Save updated user
            return Optional.of(user);
        }
        return Optional.empty();  // Return empty if user not found
    }

    /**
     * Authenticates a user using Base64 encoded credentials.
     * @param email The user's email.
     * @param password The user's password.
     * @return A map containing the authentication token if successful.
     * @throws RuntimeException If the credentials are invalid or the user is not found.
     */
    public Map<String, String> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                String token = Base64.getEncoder().encodeToString((email + ":" + password).getBytes());

                // Restituisci la risposta con la chiave "token" = " "token": "bWFyaW9yb3NzQGV4YW1wbGUuY29tOm5ld3Bhc3N3b3Jk""
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return response;
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        }
        throw new RuntimeException("User not found");
    }

    /**
     * Soft deletes a user by setting isDeleted to true.
     * @param id The ID of the user to delete.
     * @return True if the user was found and deleted, false otherwise.
     */
    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDeleted(true);  // Logically delete by setting isDeleted to true
            userRepository.save(user);
            return true; // Indicates that the user was deleted successfully
        }

        return false; // Indicates that the user was not found
    }

    /**
     * Restores a previously deleted user by setting isDeleted to false.
     * @param id The ID of the user to restore.
     * @return True if the user was found and restored, false otherwise.
     */
    public boolean restoreUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDeleted(false);  // Mark the user as not deleted
            userRepository.save(user);
            return true; // Indicates that the user was restored successfully
        }

        return false; // Indicates that the user was not found
    }

    /**
     * Retrieves the activity history of a user by their ID.
     * @param userId The ID of the user.
     * @return A list of activities performed by the user.
     */
    public List<Activity> getUserActivityHistory(Long userId) {
        return activityService.getUserActivityHistory(userId); // Delegate the responsibility to ActivityService
    }

    /**
     * Retrieves the dashboard metrics for a user, such as total energy produced and CO2 saved.
     * @param userId The ID of the user.
     * @return A map containing the dashboard metrics.
     */
    public Map<String, Long> getDashboardMetrics(Long userId) {
        return activityService.getDashboardMetrics(userId); // Delegate the responsibility to ActivityService
    }

}

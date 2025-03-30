package com.develhope.greenripple.service;

import com.develhope.greenripple.model.User;
import com.develhope.greenripple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

// Service class for handling User business logic
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new user (registration).
     * @param user The user object to be created.
     * @return The saved user object.
     */
    public User createUser(User user) {
        return userRepository.save(user);
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

            User userSaved = userRepository.save(existingUser.get());
            return Optional.of(userSaved);
        }
        return Optional.empty();
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
            return true; // Indica che l'utente è stato eliminato con successo
        }

        return false; // Indica che l'utente non è stato trovato
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
            return true; // Indica che l'utente è stato ripristinato con successo
        }

        return false; // Indica che l'utente non è stato trovato
    }

    // Retrieve a dummy user activity history
//    public List<String> getUserActivityHistory(Long id) {
//        // In a real application, activities would be retrieved from a log or a dedicated data source.
//        return Arrays.asList("Registration", "Login", "Profile Update", "Green Points Added");
//    }

//    // Return dummy dashboard metrics: CO2 avoided and energy produced
//    public Map<String, Object> getDashboardMetrics() {
//        Map<String, Object> dashboard = new HashMap<>();
//        dashboard.put("CO2Avoided", 1200);      // Dummy value
//        dashboard.put("EnergyProduced", 3500);    // Dummy value
//        return dashboard;
//    }


}

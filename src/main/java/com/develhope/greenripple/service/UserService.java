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

    // Create a new user (registration)
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Get all users, excluding logically deleted users
    public List<User> getAllUsers() {
        // Method to get only non-deleted users
        return userRepository.findByIsDeletedFalse();
    }

    // Retrieve a user by email (for login)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email); // Modified to avoid exception
    }

    // Retrieve a user by ID (profile)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Update an existing user profile
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

    // Increment green points for a user
    public Optional<User> increaseGreenPoints(Long id, int points) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Increment green points
            user.setGreenPoints(user.getGreenPoints() + points);

            userRepository.save(user);

            return Optional.of(user);
        }
        return Optional.empty();
    }

    // Decrement green points for a user
    public Optional<User> decreaseGreenPoints(Long id, int points) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setGreenPoints(user.getGreenPoints() - points);  // Decrease green points
            userRepository.save(user);  // Save updated user
            return Optional.of(user);
        }
        return Optional.empty();  // Return empty if user not found
    }

    // Login user with simplified Base64 authentication
    // Example:
    // If the user's email is "mariorossi@example.com" and the password is "securepass",
    // the concatenated string "mariorossi@example.com:securepass" is encoded into Base64.
    // The result would be "bWFyaW9yb3NzQGV4YW1wbGUuY29tOnNlY3VyZXBhc3M=" which serves as the token for authentication.

    public Map<String, String> login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
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

    // Soft delete a user (set isDeleted to true)
    public boolean deleteUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setDeleted(true);  // Logically delete by setting isDeleted to true
            userRepository.save(user);
            return true; // Indica che l'utente è stato eliminato con successo
        }

        return false; // Indica che l'utente non è stato trovato
    }

    // Restore a deleted user (set isDeleted to false)
    public boolean restoreUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
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

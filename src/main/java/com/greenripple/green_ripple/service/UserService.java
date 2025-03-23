package com.greenripple.green_ripple.service;

import com.greenripple.green_ripple.exceptions.UserNotFoundException;
import com.greenripple.green_ripple.model.User;
import com.greenripple.green_ripple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

// Service class for handling User business logic
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create a new user (registration)
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Get all users, excluding logically deleted users (where isDeleted is true)
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> !user.isDeleted())  // Filter out logically deleted users
                .collect(Collectors.toList());
    }

    // Retrieve a user by email (for login)
    public Optional<User> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        return user;
    }

    // Retrieve a user by ID (profile)
    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        return user;
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

            // Update additional fields if needed
            return Optional.of(userSaved);
        }
        // If user is not found, throw a custom exception
        throw new UserNotFoundException("User with ID " + id + " not found");
    }

    // Update the green points for a user
    public User updateGreenPoints(Long id, int points) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setGreenPoints(points);
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found");
    }

    // Login user with simplified Base64 authentication
    // Example:
    // If the user's email is "mariorossi@example.com" and the password is "securepass",
    // the concatenated string "mariorossi@example.com:securepass" is encoded into Base64.
    // The result would be "bWFyaW9yb3NzQGV4YW1wbGUuY29tOnNlY3VyZXBhc3M=" which serves as the token for authentication.

    public  Map<String, String> login(String email, String password) {
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
    public void deleteUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setDeleted(true);  // Logically delete by setting isDeleted to true
            userRepository.save(user); // Save the user with updated status
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Restore a deleted user (set isDeleted to false)
    public void restoreUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setDeleted(false);  // Mark the user as not deleted
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
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
}

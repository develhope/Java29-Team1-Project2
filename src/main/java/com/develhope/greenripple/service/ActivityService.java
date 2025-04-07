package com.develhope.greenripple.service;

import com.develhope.greenripple.model.Activity;
import com.develhope.greenripple.model.User;
import com.develhope.greenripple.repository.ActivityRepository;
import com.develhope.greenripple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new activity for user
    public Optional<Activity> createActivity(Long userId, Activity activity) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            activity.setUser(user.get());
            Activity savedActivity = activityRepository.save(activity);
            return Optional.of(savedActivity);
        }

        return Optional.empty();
    }

    // Get all activities
    public List<Activity> getAllActivities() {
        // Method to get only non-deleted users
        return activityRepository.findAll();
    }

    // Retrieve an activity by ID
    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    // Update an existing activity
    public Optional<Activity> updateActivity(Long id, Activity updatedActivity) {
        Optional<Activity> existingActivity = activityRepository.findById(id);

        if (existingActivity.isPresent()) {
            existingActivity.get().setName(updatedActivity.getName());
            existingActivity.get().setDate(updatedActivity.getDate());
            existingActivity.get().setActivityType(updatedActivity.getActivityType());
            existingActivity.get().setProducedEnergy(updatedActivity.getProducedEnergy());
            existingActivity.get().setSavedCO2Grams(updatedActivity.getSavedCO2Grams());

            Activity savedActivity = activityRepository.save(existingActivity.get());
            return Optional.of(savedActivity);
        }

        return Optional.empty();
    }
}

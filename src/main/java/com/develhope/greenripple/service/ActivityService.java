package com.develhope.greenripple.service;

import com.develhope.greenripple.dto.ActivityDTO;
import com.develhope.greenripple.model.Activity;
import com.develhope.greenripple.model.User;
import com.develhope.greenripple.repository.ActivityRepository;
import com.develhope.greenripple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new Activity for a specific User.
     *
     * @param userId   The ID of the user who performed the activity.
     * @param activity The activity object to be saved.
     * @return An Optional containing the saved activity if the user exists, otherwise empty.
     */
    public Optional<Activity> createActivity(Long userId, Activity activity) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            activity.setUser(user.get());
            Activity savedActivity = activityRepository.save(activity);
            return Optional.of(savedActivity);
        }

        return Optional.empty();
    }

    /**
     * Retrieves all activities.
     *
     * @return A list of all activities.
     */
    public List<Activity> getAllActivities() {
        // Method to get only non-deleted users
        return activityRepository.findAll();
    }

    /**
     * Retrieves an Activity by its ID.
     *
     * @param id The ID of the activity to find.
     * @return An Optional containing the activity if found, otherwise empty.
     */
    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    /**
     * Retrieves all activities performed by a specific User.
     *
     * @param userId The ID of the user.
     * @return A list of activities performed by the user.
     */
    public List<Activity> getActivitiesByUserId(Long userId) {
        return activityRepository.findByUserId(userId);
    }

    /**
     * Retrieves all activities within a given date range.
     *
     * @param startDate The start of the date range.
     * @param endDate   The end of the date range.
     * @return A list of activities within the date range.
     */
    public List<Activity> getActivitiesByDateRange(OffsetDateTime startDate, OffsetDateTime endDate) {
        return activityRepository.findByDateBetween(startDate, endDate);
    }

    /**
     * Updates an existing Activity.
     *
     * @param id              The ID of the activity to update.
     * @param updatedActivity The updated activity object.
     * @return An Optional containing the updated activity if found, otherwise empty.
     */
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

    /**
     * Retrieves all activities performed by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of activities performed by the user.
     */
    public List<Activity> getUserActivityHistory(Long userId) {
        return activityRepository.findByUserId(userId);  // Recupera tutte le attività per un determinato utente
    }

    /**
     * Retrieves the dashboard metrics for a user, such as total energy produced and CO2 saved.
     *
     * @param userId The ID of the user.
     * @return A map containing the dashboard metrics.
     */
    public Map<String, Long> getDashboardMetrics(Long userId) {
        // Retrieves all activities for a given user
        List<Activity> activities = activityRepository.findByUserId(userId);

        long totalEnergyProduced = 0L;
        long totalCO2Saved = 0L;

        // Calculates the total energy produced and CO2 saved using a for loop
        for (Activity activity : activities) {
            totalEnergyProduced += activity.getProducedEnergy();
            totalCO2Saved += activity.getSavedCO2();
        }

        // Use a HashMap to collect the metrics
        Map<String, Long> metrics = new HashMap<>();
        metrics.put("TotalEnergyProduced", totalEnergyProduced);
        metrics.put("TotalCO2Saved", totalCO2Saved);

        return metrics; // Returns the metrics map
    }

    public List<ActivityDTO> getActivitiesByUserIdAndDateRange(Long userId, OffsetDateTime startDate, OffsetDateTime endDate) {
        List<Activity> activities = activityRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        List<ActivityDTO> activityDTOs = new ArrayList<>();
        for (Activity activity : activities) {
            activityDTOs.add(mapToDTO(activity));
        }

        return activityDTOs;
    }

    private ActivityDTO mapToDTO(Activity activity) {
        return new ActivityDTO(
                activity.getId(),
                activity.getName(),
                activity.getDate(),
                activity.getProducedEnergy(),
                activity.getSavedCO2(),
                activity.getActivityType(),
                activity.getUser().getId(),
                activity.getUser().getName()
        );
    }

}

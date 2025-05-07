package com.develhope.greenripple.controller;


import com.develhope.greenripple.model.Activity;
import com.develhope.greenripple.model.googleMaps.GoogleMapsRoute;
import com.develhope.greenripple.service.ActivityService;
import com.develhope.greenripple.service.maps.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private GoogleMapsService googleMapsService;

    // Create a new Activity
    @PostMapping("/create")
    public ResponseEntity<?> createActivity(
            @RequestParam Long userId,
            @RequestParam(required = false) Double startLatitude,
            @RequestParam(required = false) Double startLongitude,
            @RequestParam(required = false) Double endLatitude,
            @RequestParam(required = false) Double endLongitude,
            @RequestBody Activity activity) {

        try {

            // Calculate and set saved CO2
            GoogleMapsRoute route = googleMapsService.calculateRoute(startLatitude, startLongitude, endLatitude, endLongitude);
            activity.setSavedCO2Grams(route.getCarCO2Grams());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Save activity
        Optional<Activity> createdActivity = activityService.createActivity(userId, activity);

        if (createdActivity.isPresent()) {
            return new ResponseEntity<>(createdActivity.get(), HttpStatus.CREATED);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User with ID '" + userId + "' not found. Cannot create activity.");
    }

    // Get all activities
    @GetMapping("/get-all")
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }


    // Get activity by ID
    @GetMapping("/get-by-activity-id/{activityId}")
    public ResponseEntity<?> getActivityById(@PathVariable("activityId") Long id) {
        Optional<Activity> activity = activityService.getActivityById(id);

        if (activity.isPresent()) {
            return ResponseEntity.ok(activity.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Activity with ID '" + id + "' not found.");
    }

    // Get activities by user ID
    @GetMapping("/get-by-user/{userId}")
    public  ResponseEntity<?> getActivitiesByUserId(@PathVariable Long userId) {
        List<Activity> activities = activityService.getActivitiesByUserId(userId);

        if (!activities.isEmpty()) {
            return ResponseEntity.ok(activities);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No activities found for user ID '" + userId + "'.");
    }

    // Get activities within a date range
    @GetMapping("/get-by-date-range")
    public ResponseEntity<?> getActivitiesByDateRange(
            @RequestParam("userId") Long userId,
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr
    ) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Parsing a LocalDate
            LocalDate startDateLocal = LocalDate.parse(startDateStr, formatter);
            LocalDate endDateLocal = LocalDate.parse(endDateStr, formatter);

            // Conversione in OffsetDateTime (fuso orario Europa/Rome)
            ZoneId zoneId = ZoneId.of("Europe/Rome");
            OffsetDateTime startDate = startDateLocal.atStartOfDay(zoneId).toOffsetDateTime();
            OffsetDateTime endDate = endDateLocal.atTime(23, 59, 59).atZone(zoneId).toOffsetDateTime();

            List<Activity> activities = activityService.getActivitiesByUserIdAndDateRange(userId, startDate, endDate);

            if (!activities.isEmpty()) {
                return ResponseEntity.ok(activities);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No activities found for user ID '" + userId + "' in the given date range.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Update an existing activity
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateActivity(@PathVariable Long id, @RequestBody Activity updatedActivity) {
        try {
            Optional<Activity> optionalActivity = activityService.updateActivity(id, updatedActivity);

            if (optionalActivity.isPresent()) {
                return ResponseEntity.ok(optionalActivity.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Activity with ID '" + id + "' not found.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid activity type: " + e.getMessage());
        }
    }


}

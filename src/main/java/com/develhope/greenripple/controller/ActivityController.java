package com.develhope.greenripple.controller;

import com.develhope.greenripple.model.Activity;
import com.develhope.greenripple.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    // Create a new Activity
    @PostMapping("/create-for-user/{userId}")
    public ResponseEntity<Activity> createActivity(@PathVariable Long userId, @RequestBody Activity activity) {
        Optional<Activity> createdActivity = activityService.createActivity(userId, activity);

        if (createdActivity.isPresent()) {
            return new ResponseEntity<>(createdActivity.get(), HttpStatus.CREATED);
        }

        return ResponseEntity.notFound().build();
    }

    // Get all activities
    @GetMapping("/get-all")
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }


    // Get activity by ID
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        Optional<Activity> activity = activityService.getActivityById(id);

        if (activity.isPresent()) {
            return ResponseEntity.ok(activity.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Update an existing activity
    @PutMapping("/update/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity updatedActivity) {
        Optional<Activity> optionalActivity = activityService.updateActivity(id, updatedActivity);

        if (optionalActivity.isPresent()) {
            return ResponseEntity.ok(optionalActivity.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}

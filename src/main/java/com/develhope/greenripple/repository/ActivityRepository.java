package com.develhope.greenripple.repository;

import com.develhope.greenripple.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // Get all activities for a specific user
    List<Activity> findByUserId(Long userId);

    // Get activities within a date range
    List<Activity> findByDateBetween(OffsetDateTime startDate, OffsetDateTime endDate);

    // Get activities within a date range of a specific User
    List<Activity> findByUserIdAndDateBetween(Long userId, OffsetDateTime startDate, OffsetDateTime endDate);

}

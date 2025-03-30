package com.develhope.greenripple.repository;

import com.develhope.greenripple.model.UserReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRewardRepository extends JpaRepository<UserReward, Long> {

    // Retrieve all user rewards sorted from the most recent to the oldest
    @Query("SELECT ur FROM UserReward ur WHERE ur.user.id = :userId ORDER BY ur.redeemedAt DESC")
    List<UserReward> findByUserId(Long userId);
}

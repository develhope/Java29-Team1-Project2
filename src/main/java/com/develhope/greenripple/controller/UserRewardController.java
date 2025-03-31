package com.develhope.greenripple.controller;

import com.develhope.greenripple.model.User;
import com.develhope.greenripple.model.UserReward;
import com.develhope.greenripple.service.UserRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("user-rewards")
public class UserRewardController {

    @Autowired
    private UserRewardService userRewardService;

    // Endpoint to redeem a reward for a user
    @PostMapping("/redeem/{userId}/{rewardId}")
    public ResponseEntity<?> redeemReward(@PathVariable Long userId, @PathVariable Long rewardId) {
        try {
            // Attempt to redeem the reward
            Optional<UserReward> userReward = userRewardService.redeemReward(userId, rewardId);

            if (userReward.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Return the redeemed reward with HTTP 200 (OK)
            return ResponseEntity.ok(userReward.get());
        } catch (RuntimeException e) {
            // If an error occurs (not enough points, reward not available, etc.), return HTTP 400 (Bad Request)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // Endpoint to retrieve all rewards redeemed by a specific user
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserReward>> getUserRewards(@PathVariable Long userId) {
        List<UserReward> userRewards = userRewardService.getUserRewards(userId);

        if (userRewards.isEmpty()) {
            // If no rewards were found for the user, return a 204 No Content status
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // If rewards are found, return them with a 200 OK status
        return new ResponseEntity<>(userRewards, HttpStatus.OK);
    }
}

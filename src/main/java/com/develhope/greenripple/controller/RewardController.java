package com.develhope.greenripple.controller;

import com.develhope.greenripple.exceptions.reward.RewardNotFoundException;
import com.develhope.greenripple.model.Reward;
import com.develhope.greenripple.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    // Create a new reward
    @PostMapping("/create")
    public ResponseEntity<Reward> createReward(@RequestBody Reward reward) {
        Reward createdReward = rewardService.createReward(reward);
        return new ResponseEntity<>(createdReward, HttpStatus.CREATED);
    }

    // Create multiple rewards at once
    @PostMapping("/create-multiple")
    public ResponseEntity<List<Reward>> createMultipleRewards(@RequestBody List<Reward> rewards) {
        List<Reward> createdRewards = rewardService.createMultipleRewards(rewards);
        return new ResponseEntity<>(createdRewards, HttpStatus.CREATED);
    }

    // Get all rewards
    @GetMapping("/get-all")
    public ResponseEntity<List<Reward>> getAllRewards() {
        List<Reward> rewards = rewardService.getAllRewards();
        return ResponseEntity.ok(rewards);
    }

    // Get reward by ID
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Reward> getRewardById(@PathVariable Long id) {
        Optional<Reward> reward = rewardService.getRewardById(id);

        if (reward.isEmpty()) {
            throw new RewardNotFoundException(id);
        }
        return ResponseEntity.ok(reward.get());
    }

    // Update an existing reward
    @PutMapping("/update/{id}")
    public ResponseEntity<Reward> updateReward(@PathVariable Long id, @RequestBody Reward updatedReward) {
        Optional<Reward> optionalReward = rewardService.updateReward(id, updatedReward);

        if (optionalReward.isPresent()) {
            return ResponseEntity.ok(optionalReward.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Delete a reward by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReward(@PathVariable Long id) {
        rewardService.deleteReward(id);
        return ResponseEntity.ok("Reward deleted successfully");
    }

    @PutMapping("/redeem")
    public ResponseEntity<?> redeemReward(
            @RequestParam Long userId,
            @RequestParam Long rewardId
    ) {
        try {

            Reward reward = rewardService.redeemReward(userId, rewardId);
            return ResponseEntity.ok(reward);

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
    }
}

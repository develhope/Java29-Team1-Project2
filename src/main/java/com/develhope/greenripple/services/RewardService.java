package com.develhope.greenripple.services;

import com.develhope.greenripple.exceptions.reward.RewardNotAvailableException;
import com.develhope.greenripple.exceptions.reward.RewardNotFoundException;
import com.develhope.greenripple.exceptions.user.UserNotFoundException;
import com.develhope.greenripple.entities.Reward;
import com.develhope.greenripple.entities.User;
import com.develhope.greenripple.repositories.RewardRepository;
import com.develhope.greenripple.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RewardService {

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new Reward and save it to the database.
     * @param reward The reward to be created.
     * @return The saved reward.
     */
    public Reward createReward(Reward reward) {
        return rewardRepository.save(reward);
    }

    /**
     * Create multiple rewards at once and save them to the database.
     * @param rewards A list of rewards to be created.
     * @return The list of saved rewards.
     */
    public List<Reward> createMultipleRewards(List<Reward> rewards) {
        return rewardRepository.saveAll(rewards);
    }

    /**
     * Retrieve all rewards stored in the database.
     * @return A list of all available rewards.
     */
    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }

    /**
     * Find a reward by its unique ID.
     * @param id The ID of the reward.
     * @return An optional containing the found reward, or empty if not found.
     */
    public Optional<Reward> getRewardById(Long id) {
        return rewardRepository.findById(id);
    }

    /**
     * Update an existing reward with new details.
     * @param id The ID of the reward to be updated.
     * @param updatedReward The new details to update the reward with.
     * @return An optional containing the updated reward, or empty if not found.
     */
    public Optional<Reward> updateReward(Long id, Reward updatedReward) {
        Optional<Reward> existingReward = rewardRepository.findById(id);

        // If the reward exists, update its fields
        if (existingReward.isPresent()) {

            existingReward.get().setName(updatedReward.getName());
            existingReward.get().setDescription(updatedReward.getDescription());
            existingReward.get().setRequiredGreenPoints(updatedReward.getRequiredGreenPoints());

            // Save the updated reward
            Reward rewardSaved = rewardRepository.save(existingReward.get());
            return Optional.of(rewardSaved);
        }
        return Optional.empty();  // Return empty if the reward is not found
    }

    /**
     * Delete a reward by its ID.
     * @param id The ID of the reward to be deleted.
     */
    public void deleteReward(Long id) {
        rewardRepository.deleteById(id);
    }

    /**
     * Redeems a reward by its ID
     * @param userId is the ID of the user who redeems the reward
     * @param rewardId is the ID of the reward to redeem
     * @return the redeemed reward
     */
    public Reward redeemReward(Long userId, Long rewardId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Reward> reward = rewardRepository.findById(rewardId);

        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        if (reward.isEmpty()) {
            throw new RewardNotFoundException(rewardId);
        }

        if (reward.get().getRedeemedBy() != null) {
            throw new RewardNotAvailableException("Reward with id " + rewardId + " has already been redeemed");
        }

        reward.get().setRedeemedBy(user.get());
        return rewardRepository.save(reward.get());
    }
}

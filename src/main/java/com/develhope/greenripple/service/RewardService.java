package com.develhope.greenripple.service;

import com.develhope.greenripple.exceptions.reward.RewardNotAvailableException;
import com.develhope.greenripple.exceptions.reward.RewardNotFoundException;
import com.develhope.greenripple.exceptions.user.UserNotFoundException;
import com.develhope.greenripple.model.Reward;
import com.develhope.greenripple.model.User;
import com.develhope.greenripple.repository.RewardRepository;
import com.develhope.greenripple.repository.UserRepository;
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
            // Check if requiredGreenPoints is negative
            if (updatedReward.getRequiredGreenPoints() == null || updatedReward.getRequiredGreenPoints() < 0) {
                throw new IllegalArgumentException("Required green points must be a positive number");
            }

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
        if (!rewardRepository.existsById(id)) {
            throw new RewardNotFoundException(id);
        }
        rewardRepository.deleteById(id);
    }

    /**
     * Redeems a reward for a specific user by its ID.
     *
     * This method checks if the user and reward exist, ensures the reward
     * has not already been redeemed, and verifies that the user has enough
     * green points to redeem the reward. If all conditions are met, it deducts
     * the required points from the user and marks the reward as redeemed.
     *
     * @param userId    the ID of the user attempting to redeem the reward
     * @param rewardId  the ID of the reward to be redeemed
     * @return the redeemed Reward entity
     * @throws UserNotFoundException        if the user with the given ID does not exist
     * @throws RewardNotFoundException      if the reward with the given ID does not exist
     * @throws RewardNotAvailableException  if the reward has already been redeemed or
     *                                      the user does not have enough green points
     */
    public Reward redeemReward(Long userId, Long rewardId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Reward> rewardOpt = rewardRepository.findById(rewardId);

        // Check if the user exists
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        // Check if the reward exists
        if (rewardOpt.isEmpty()) {
            throw new RewardNotFoundException(rewardId);
        }

        User user = userOpt.get();
        Reward reward = rewardOpt.get();

        // Check if the reward has already been redeemed
        if (reward.getRedeemedBy() != null) {
            throw new RewardNotAvailableException("Reward with id " + rewardId + " has already been redeemed");
        }

        // ✅ Check if the user has enough green points to redeem the reward
        if (user.getGreenPoints() < reward.getRequiredGreenPoints()) {
            throw new RewardNotAvailableException(
                    "User does not have enough green points to redeem this reward. Required: "
                            + reward.getRequiredGreenPoints() + ", available: " + user.getGreenPoints()
            );
        }

        // 🔻  Deduct the required points from the user's green points
        user.setGreenPoints(user.getGreenPoints() - reward.getRequiredGreenPoints());

        // ✅ Mark the reward as redeemed by the user
        reward.setRedeemedBy(user);

        // 💾 Save the updated user (with updated green points)
        userRepository.save(user);

        // 💾 Save the redeemed reward
        return rewardRepository.save(reward);
    }
}

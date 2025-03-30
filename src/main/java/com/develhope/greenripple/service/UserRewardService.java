package com.develhope.greenripple.service;

import com.develhope.greenripple.model.Reward;
import com.develhope.greenripple.model.User;
import com.develhope.greenripple.model.UserReward;
import com.develhope.greenripple.repository.RewardRepository;
import com.develhope.greenripple.repository.UserRepository;
import com.develhope.greenripple.repository.UserRewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Service handling the reward redemption process
@Service
public class UserRewardService {

    @Autowired
    private UserRewardRepository userRewardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RewardRepository rewardRepository;

    /**
     * Redeems a reward for a user if they have enough green points and if the reward is available.
     * @param userId The ID of the user attempting to redeem the reward.
     * @param rewardId The ID of the reward to be redeemed.
     * @return An optional containing the redeemed reward details or empty if conditions are not met.
     * @throws RuntimeException If the user or reward is not found, or if redemption conditions are not met.
     */
    public Optional<UserReward> redeemReward(Long userId, Long rewardId) {
        // Retrieve the user and reward from the database
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Reward> rewardOptional = rewardRepository.findById(rewardId);

        // Check if the user exists
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // Check if the reward exists
        if (rewardOptional.isEmpty()) {
            throw new RuntimeException("Reward not found with ID: " + rewardId);
        }

        // Extract user and reward objects
        User user = userOptional.get();
        Reward reward = rewardOptional.get();

        // Check if the user has enough green points to redeem the reward
        if (user.getGreenPoints() < reward.getRequiredGreenPoints()) {
            throw new RuntimeException("Not enough green points! User has: " + user.getGreenPoints() +
                    ", but needs: " + reward.getRequiredGreenPoints());
        }

        // Check if the reward is still available (quantity > 0)
        if (reward.getQuantity() <= 0) {
            throw new RuntimeException("Reward is out of stock!");
        }

        // Deduct the required green points from the user's balance
        user.setGreenPoints(user.getGreenPoints() - reward.getRequiredGreenPoints());

        // Reduce the stock of the reward by 1
        reward.setQuantity(reward.getQuantity() - 1);

        // Create a new UserReward entry (the redeemedAt timestamp is automatically set)
        UserReward userReward = new UserReward(user, reward);
        userRewardRepository.save(userReward);

        // Save the updated user and reward entities to the database
        userRepository.save(user);
        rewardRepository.save(reward);

        // Return the redeemed reward
        return Optional.of(userReward);
    }

    /**
     * Retrieves all rewards redeemed by a specific user.
     * @param userId The ID of the user whose rewards are being retrieved.
     * @return A list of all rewards redeemed by the user.
     */
    public List<UserReward> getUserRewards(Long userId) {
        return userRewardRepository.findByUserId(userId);
    }
}

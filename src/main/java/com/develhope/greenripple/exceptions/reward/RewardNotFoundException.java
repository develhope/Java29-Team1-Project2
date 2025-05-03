package com.develhope.greenripple.exceptions.reward;

public class RewardNotFoundException extends RuntimeException {
    public RewardNotFoundException(Long id) {
        super("Reward with id " + id + " not found");
    }
}

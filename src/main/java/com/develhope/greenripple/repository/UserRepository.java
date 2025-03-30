package com.develhope.greenripple.repository;

import com.develhope.greenripple.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for User entity

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Optional method to find User by Email
    Optional<User> findByEmail(String email);

    // Optional method to find non-deleted users
    Optional<User> findByEmailAndIsDeletedFalse(String email);

    // Custom method to get active users
    List<User> findByIsDeletedFalse();
}

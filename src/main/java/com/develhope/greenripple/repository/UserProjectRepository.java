package com.develhope.greenripple.repository;

import com.develhope.greenripple.model.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    // Retrieve all user voted projects sorted from the most recent to the oldest
    List<UserProject> findByUserIdOrderByVotedAtDesc(Long userId);
}

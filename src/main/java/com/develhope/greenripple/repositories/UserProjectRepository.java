package com.develhope.greenripple.repositories;

import com.develhope.greenripple.entities.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    // Retrieve all user voted projects sorted from the most recent to the oldest
    List<UserProject> findByUserIdOrderByVotedAtDesc(Long userId);
}

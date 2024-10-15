package com.backend.spring.repositories;

import com.backend.spring.entities.User;
import com.backend.spring.entities.UserGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGoalRepository extends JpaRepository<UserGoal, Integer> {
    Optional<UserGoal> findByUser(User user);

}


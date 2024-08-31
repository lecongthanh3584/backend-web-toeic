package com.backend.spring.repository;

import com.backend.spring.entity.User;
import com.backend.spring.entity.UserGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGoalRepository extends JpaRepository<UserGoal, Integer> {
    Optional<UserGoal> findByUser(User user);

}


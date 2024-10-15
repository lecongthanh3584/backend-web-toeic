package com.backend.spring.services.UserGoal;

import com.backend.spring.entities.UserGoal;
import com.backend.spring.payload.request.UserGoalRequest;

import java.util.List;

public interface IUserGoalService {
    List<UserGoal> getAllUserGoals();
    UserGoal createUserGoal(UserGoalRequest userGoalRequest);
    UserGoal updateUserGoalByUserId(UserGoalRequest userGoalRequest);
    UserGoal getUserGoalById(Integer userGoalId);
    UserGoal getUserGoalByUserId(Integer userId);
    boolean hasUserGoalWithUserId(Integer userId);



}

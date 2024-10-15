package com.backend.spring.mapper;

import com.backend.spring.entities.UserGoal;
import com.backend.spring.payload.response.UserGoalResponse;

public class UserGoalMapper {
    public static UserGoalResponse mapFromEntityToResponse(UserGoal userGoal) {
        if(userGoal == null) {
            return null;
        }

        return new UserGoalResponse(
                userGoal.getUserGoalId(),
                userGoal.getGoalScore(),
                userGoal.getCreatedAt(),
                userGoal.getUpdatedAt(),
                UserMapper.mapFromEntityToResponse(userGoal.getUser())
        );
    }
}

package com.backend.spring.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGoalResponse {
    private Integer userGoalId;

    private Integer goalScore;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserResponse user;
}

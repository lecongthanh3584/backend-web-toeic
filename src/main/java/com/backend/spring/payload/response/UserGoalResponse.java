package com.backend.spring.payload.response;

import com.backend.spring.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

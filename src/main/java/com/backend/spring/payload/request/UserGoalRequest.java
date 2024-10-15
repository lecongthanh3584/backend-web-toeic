package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserGoalRequest {
    private Integer userGoalId;

    @NotNull(message = "Điểm mục tiêu không được bỏ trống!")
    private Integer goalScore;
}


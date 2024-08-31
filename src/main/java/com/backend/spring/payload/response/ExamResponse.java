package com.backend.spring.payload.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ExamResponse {
    private Integer examId;

    private String examName;

    private Integer examType;

    private Integer examDuration;

    private Integer examStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

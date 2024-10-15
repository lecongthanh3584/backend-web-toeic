package com.backend.spring.payload.response;

import com.backend.spring.entities.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TestResponse {
    private Integer testId;

    private String testName;

    private Integer testParticipants;

    private Integer testStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Section section;
}

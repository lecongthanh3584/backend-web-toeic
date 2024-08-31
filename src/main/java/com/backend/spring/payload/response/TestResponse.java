package com.backend.spring.payload.response;

import com.backend.spring.entity.Question;
import com.backend.spring.entity.Section;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

package com.backend.spring.payload.response;

import com.backend.spring.entity.Section;
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

public class LessonResponse {
    private Integer lessonId;

    private String lessonName;

    private Integer lessonStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Section section;
}

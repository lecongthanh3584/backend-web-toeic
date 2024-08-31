package com.backend.spring.payload.response;

import com.backend.spring.entity.Lesson;
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

public class LessonContentResponse {
    private Integer contentId;

    private String title;

    private String content;

    private Integer lessonContentStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Lesson lesson;
}

package com.backend.spring.payload.response;

import com.backend.spring.entities.Lesson;
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

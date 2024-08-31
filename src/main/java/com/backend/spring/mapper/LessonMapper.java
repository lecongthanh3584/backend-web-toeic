package com.backend.spring.mapper;

import com.backend.spring.entity.Lesson;
import com.backend.spring.payload.response.LessonResponse;

public class LessonMapper {
    public static LessonResponse mapFromEntityToResponse(Lesson lesson) {
        if(lesson == null) {
            return null;
        }

        return new LessonResponse(
                lesson.getLessonId(),
                lesson.getLessonName(),
                lesson.getLessonStatus(),
                lesson.getCreatedAt(),
                lesson.getUpdatedAt(),
                lesson.getSection()
        );
    }
}

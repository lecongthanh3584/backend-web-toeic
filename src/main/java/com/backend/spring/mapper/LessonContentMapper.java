package com.backend.spring.mapper;

import com.backend.spring.entities.LessonContent;
import com.backend.spring.payload.response.LessonContentResponse;

public class LessonContentMapper {
    public static LessonContentResponse mapFromEntityToResponse(LessonContent lessonContent) {
        if(lessonContent == null) {
            return null;
        }

        return new LessonContentResponse(
                lessonContent.getContentId(),
                lessonContent.getTitle(),
                lessonContent.getContent(),
                lessonContent.getLessonContentStatus(),
                lessonContent.getCreatedAt(),
                lessonContent.getUpdatedAt(),
                lessonContent.getLesson()
        );
    }
}

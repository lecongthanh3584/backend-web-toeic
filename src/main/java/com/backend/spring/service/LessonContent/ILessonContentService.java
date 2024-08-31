package com.backend.spring.service.LessonContent;

import com.backend.spring.entity.LessonContent;
import com.backend.spring.payload.request.LessonContentRequest;

import java.util.List;

public interface ILessonContentService {
    LessonContent createLessonContent(LessonContentRequest lessonContentRequest);
    LessonContent updateLessonContent(LessonContentRequest lessonContentRequest);
    LessonContent updateLessonContentStatus(Integer id, Integer newStatus);
    List<LessonContent> getAllLessonContents();
    LessonContent getLessonContentById(Integer id);
    boolean deleteLessonContent(Integer id);
    List<LessonContent> getLessonContentsByLessonId(Integer lessonId);

}

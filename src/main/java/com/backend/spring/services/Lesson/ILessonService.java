package com.backend.spring.services.Lesson;

import com.backend.spring.entities.Lesson;
import com.backend.spring.payload.request.LessonRequest;

import java.util.List;

public interface ILessonService {
    List<Lesson> getAllLessons();
    Lesson getLessonById(Integer id);
    Lesson createLesson(LessonRequest lessonRequest);
    boolean deleteLesson(Integer id);
    Lesson updateLesson(LessonRequest lessonRequest);
    Lesson updateLessonStatus(Integer id, Integer newStatus);
    List<Lesson> getLessonsBySectionId(Integer sectionId);
    String getLessonNameById(Integer id);
}

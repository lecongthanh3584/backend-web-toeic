package com.backend.spring.repositories;

import com.backend.spring.entities.Lesson;
import com.backend.spring.entities.LessonContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonContentRepository extends JpaRepository<LessonContent, Integer> {
    List<LessonContent> findByLesson(Lesson lesson);
}

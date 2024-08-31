package com.backend.spring.repository;

import com.backend.spring.entity.Lesson;
import com.backend.spring.entity.LessonContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonContentRepository extends JpaRepository<LessonContent, Integer> {
    List<LessonContent> findByLesson(Lesson lesson);
}

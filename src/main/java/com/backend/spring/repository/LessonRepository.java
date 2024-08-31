package com.backend.spring.repository;

import com.backend.spring.entity.Lesson;
import com.backend.spring.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findBySection(Section section);
}

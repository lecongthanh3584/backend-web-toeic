package com.backend.spring.repositories;

import com.backend.spring.entities.Lesson;
import com.backend.spring.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findBySection(Section section);
}

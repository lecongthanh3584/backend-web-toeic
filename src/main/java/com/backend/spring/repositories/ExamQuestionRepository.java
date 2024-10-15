package com.backend.spring.repositories;

import com.backend.spring.entities.ExamQuestion;
import com.backend.spring.entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Integer> {
    List<ExamQuestion> findByExam(Exam exam);
}

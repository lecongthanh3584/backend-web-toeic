package com.backend.spring.repository;

import com.backend.spring.entity.ExamQuestion;
import com.backend.spring.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Integer> {
    List<ExamQuestion> findByExam(Exam exam);
}

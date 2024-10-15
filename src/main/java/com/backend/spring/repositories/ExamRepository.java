package com.backend.spring.repositories;
import com.backend.spring.entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {

    List<Exam> findByExamType(int i);

    boolean existsByExamName(String examName);

    boolean existsByExamNameAndExamIdNot(String examName, Integer examId);
}
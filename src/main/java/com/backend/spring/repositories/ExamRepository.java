package com.backend.spring.repositories;
import com.backend.spring.entities.Exam;
import com.backend.spring.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {

    @Query(value = "SELECT * FROM exam e " +
            "WHERE e.exam_type = ?1 AND e.exam_name LIKE CONCAT('%', ?2, '%') AND e.deleted_at IS NULL", nativeQuery = true)
    Page<Exam> getExamByType(int type, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM exam e " +
            "WHERE e.exam_type = ?1 AND e.exam_status = ?2 AND e.exam_name LIKE CONCAT('%', ?3, '%') AND e.deleted_at IS NULL", nativeQuery = true)
    Page<Exam> findByExamTypeAndEnable(int type, int status, String keyword, Pageable pageable);

    boolean existsByExamName(String examName);

    boolean existsByExamNameAndExamIdNot(String examName, Integer examId);
}
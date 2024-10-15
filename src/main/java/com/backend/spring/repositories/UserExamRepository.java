package com.backend.spring.repositories;

import com.backend.spring.payload.response.DailyExamCountResponse;
import com.backend.spring.payload.response.ExamAttemptResponse;
import com.backend.spring.entities.Exam;
import com.backend.spring.entities.User;
import com.backend.spring.entities.UserExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExamRepository extends JpaRepository<UserExam, Integer> {
    List<UserExam> findByExamAndUser(Exam exam, User user);

    List<UserExam> findByUser(User user);

    boolean existsByExamAndUser(Exam exam, User user);

    @Query(value = "SELECT e.exam_name AS examName, COUNT(ue.user_exam_id) AS totalAttempts FROM user_exam ue JOIN exam e \n" +
            "ON ue.exam_id = e.exam_id WHERE e.exam_type = 1 GROUP BY e.exam_name", nativeQuery = true)
    List<ExamAttemptResponse> getNumberAttemptForEachExam();

    @Query(value = "SELECT date(ue.created_at) AS date, COUNT(ue.user_exam_id) AS quantity FROM user_exam ue \n" +
            "JOIN exam e ON ue.exam_id = e.exam_id \n" +
            "WHERE e.exam_type = 1 GROUP BY date(ue.created_at) ORDER BY date(ue.created_at) ASC", nativeQuery = true)
    List<DailyExamCountResponse> getDailyExamCount();
}


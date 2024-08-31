package com.backend.spring.repository;

import com.backend.spring.entity.UserExam;
import com.backend.spring.entity.UserExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExamQuestionRepository extends JpaRepository<UserExamQuestion, Integer> {

    List<UserExamQuestion> findByUserExam(UserExam userExam);

    @Query("SELECT ueq FROM UserExamQuestion ueq JOIN UserExam ue " +
            "ON ueq.userExam.userExamId = ue.userExamId WHERE ue.user.userId = ?1")
    List<UserExamQuestion> getUserExamQuestionByUserId(Integer userId);
}


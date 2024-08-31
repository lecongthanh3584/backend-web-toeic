package com.backend.spring.payload.response;

import com.backend.spring.entity.Exam;
import com.backend.spring.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserExamResponse {
    private Integer userExamId;

    private Integer completionTime;  //Thời gian hoàn thành, tính theo giây

    private Integer numListeningCorrectAnswers;

    private Integer listeningScore;

    private Integer numReadingCorrectAnswers;

    private Integer readingScore;

    private Integer totalScore;

    private Integer numCorrectAnswers;

    private Integer numWrongAnswers;

    private Integer numSkippedQuestions;

    private Integer goalScore;

    private LocalDateTime createdAt;

    private User user;

    private Exam exam;
}

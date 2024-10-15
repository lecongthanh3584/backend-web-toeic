package com.backend.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "user_exam")
public class UserExam extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_exam_id")
    private Integer userExamId;

    @Column(name = "completion_time", nullable = false)
    private Integer completionTime;  //Thời gian hoàn thành, tính theo giây

    @Column(name = "num_listening_correct_answers", nullable = false)
    private Integer numListeningCorrectAnswers;

    @Column(name = "listening_score", nullable = false)
    private Integer listeningScore;

    @Column(name = "num_reading_correct_answers", nullable = false)
    private Integer numReadingCorrectAnswers;

    @Column(name = "reading_score", nullable = false)
    private Integer readingScore;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @Column(name = "num_correct_answers", nullable = false)
    private Integer numCorrectAnswers;

    @Column(name = "num_wrong_answers", nullable = false)
    private Integer numWrongAnswers;

    @Column(name = "num_skipped_questions", nullable = false)
    private Integer numSkippedQuestions;

    @Column(name = "goal_score", nullable = false)
    private Integer goalScore;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    @JsonIgnore
    private Exam exam;
}



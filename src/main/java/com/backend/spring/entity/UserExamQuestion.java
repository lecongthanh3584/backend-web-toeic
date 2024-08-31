package com.backend.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "user_exam_question")
public class UserExamQuestion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_exam_question_id")
    private Integer userExamQuestionId;

    @Column(name = "selected_option", columnDefinition = "TEXT")
    private String selectedOption;

    @Column(name = "is_correct")
    private Integer isCorrect;

    @ManyToOne
    @JoinColumn(name = "user_exam_id", nullable = false)
    @JsonIgnore
    private UserExam userExam;

    @ManyToOne
    @JoinColumn(name = "exam_question_id", nullable = false)
    @JsonIgnore
    private ExamQuestion examQuestion;

}

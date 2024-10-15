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
@Table(name = "vocabulary_question")
public class VocabularyQuestion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "question_content", columnDefinition = "TEXT")
    private String questionContent;

    @Column(name = "option_a", columnDefinition = "TEXT")
    private String optionA;

    @Column(name = "option_b", columnDefinition = "TEXT")
    private String optionB;

    @Column(name = "option_c", columnDefinition = "TEXT")
    private String optionC;

    @Column(name = "option_d", columnDefinition = "TEXT")
    private String optionD;

    @Column(name = "correct_option", columnDefinition = "TEXT")
    private String correctOption;

    @Column(name = "question_explanation", columnDefinition = "TEXT")
    private String questionExplanation;

    @Column(name = "question_status", nullable = false)
    private Integer questionStatus;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    @JsonIgnore
    private Topic topic;

}

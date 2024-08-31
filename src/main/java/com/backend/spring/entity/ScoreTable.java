package com.backend.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "score_table")
public class ScoreTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_table_id")
    private Integer scoreTableId;

    @Column(name = "num_correct_answers", nullable = false)
    private Integer numCorrectAnswers;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "type", nullable = false)
    private Integer type; // 0 - listening, 1 - reading

}

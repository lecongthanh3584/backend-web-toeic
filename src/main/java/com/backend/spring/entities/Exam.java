package com.backend.spring.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam")
public class Exam extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Integer examId;

    @Column(name = "exam_name", nullable = false, unique = true)
    private String examName;

    @Column(name = "exam_type", nullable = false)
    private Integer examType; // 0 for mini, 1 for full

    @Column(name = "exam_duration", nullable = false)
    private Integer examDuration; // Thời gian duy trì theo giây

    @Column(name = "exam_status", nullable = false)
    private Integer examStatus;
}


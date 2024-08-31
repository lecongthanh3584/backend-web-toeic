package com.backend.spring.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam" ,uniqueConstraints = {
        @UniqueConstraint(columnNames = "exam_name") } )
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Integer examId;

    @Column(name = "exam_name", nullable = false, length = 255, unique = true)
    private String examName;

    @Column(name = "exam_type", nullable = false)
    private Integer examType; // 0 for mini, 1 for full

    @Column(name = "exam_duration", nullable = false)
    private Integer examDuration; // Thời gian duy trì theo giây

    @Column(name = "exam_status", nullable = false)
    private Integer examStatus;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}


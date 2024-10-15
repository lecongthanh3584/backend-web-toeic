package com.backend.spring.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson")
public class Lesson extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Integer lessonId;

    @Column(name = "lesson_name", nullable = false)
    private String lessonName;

    @Column(name = "lesson_status", nullable = false)
    private Integer lessonStatus;

    @ManyToOne
    @JoinColumn(name = "section_id", updatable = false, nullable = false)
    @JsonIgnore
    private Section section;
}

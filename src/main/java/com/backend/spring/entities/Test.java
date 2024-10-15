package com.backend.spring.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "test")
public class Test extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Integer testId;

    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "test_participants", nullable = false)
    private Integer testParticipants = 0;

    @Column(name = "test_status", nullable = false)
    private Integer testStatus;

    @ManyToOne
    @JoinColumn(name = "section_id", updatable = false)
    @JsonIgnore
    private Section section;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "test_question",
            joinColumns = @JoinColumn(name = "test_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    @JsonIgnore
    private Set<Question> questions;
}

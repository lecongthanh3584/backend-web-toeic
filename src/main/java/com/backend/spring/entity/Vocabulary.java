package com.backend.spring.entity;

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
@Table(name = "vocabulary")
public class Vocabulary implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vocabulary_id")
    private Integer vocabularyId;

    @Column(name = "word", nullable = false, length = 255)
    private String word;

    @Column(name = "ipa", nullable = false, length = 255)
    private String ipa;

    @Column(name = "meaning", nullable = false, length = 255)
    private String meaning;

    @Column(name = "example_sentence", nullable = false, length = 255)
    private String exampleSentence;

    @Column(name = "image")
    private String image;

    @Column(name = "vocabulary_status", nullable = false)
    private Integer vocabularyStatus;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    //  Nhiều từ vựng thuộc về 1 chủ đề
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    @JsonIgnore
    private Topic topic;
}

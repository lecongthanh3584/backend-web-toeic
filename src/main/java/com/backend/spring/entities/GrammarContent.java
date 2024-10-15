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
@Table(name = "grammar_content")
public class GrammarContent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Integer contentId;

    @Column(name = "title", nullable = false)
    private String title;

    // Nội dung chi tiết về loại ngữ pháp và các ví dụ.
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "grammar_content_status", nullable = false)
    private Integer grammarContentStatus;

    // Khóa ngoại liên kết với bảng "Grammar" để xác định loại ngữ pháp của nội dung này.
    @ManyToOne
    @JoinColumn(name = "grammar_id", nullable = false)
    @JsonIgnore
    private Grammar grammar;
}

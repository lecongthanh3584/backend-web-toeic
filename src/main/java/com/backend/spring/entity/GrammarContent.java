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
@Table(name = "grammar_content")
public class GrammarContent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Integer contentId;

    // Tiêu đề của nội dung (VD: "Positions of a noun", "Countable nouns & uncountable nouns",...).
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    // Nội dung chi tiết về loại ngữ pháp và các ví dụ.
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "grammar_content_status", nullable = false)
    private Integer grammarContentStatus;

    // Ngày và giờ tạo bản ghi.
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Ngày và giờ cập nhật bản ghi (nếu có).
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Khóa ngoại liên kết với bảng "Grammar" để xác định loại ngữ pháp của nội dung này.
    @ManyToOne
    @JoinColumn(name = "grammar_id", nullable = false)
    @JsonIgnore
    private Grammar grammar;
}

package com.backend.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "feedback")
public class Feedback implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Integer feedbackId;

    @Column(name = "name", nullable = false)
    private String name; // Tên người gửi phản hồi

    @Column(name = "email", nullable = false)
    private String email; // Địa chỉ email của người gửi phản hồi

    @Column(name = "review", nullable = false, columnDefinition = "TEXT")
    private String review; // Nội dung đánh giá hoặc ý kiến

    @Column(name = "rating", nullable = false)
    private int rating; // Điểm số đánh giá (có thể là số nguyên hoặc số thực, tùy theo yêu cầu)

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}

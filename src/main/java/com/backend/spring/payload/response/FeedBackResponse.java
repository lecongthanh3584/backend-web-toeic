package com.backend.spring.payload.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FeedBackResponse {
    private Integer feedbackId;

    private String name; // Tên người gửi phản hồi

    private String email; // Địa chỉ email của người gửi phản hồi

    private String review; // Nội dung đánh giá hoặc ý kiến

    private int rating; // Điểm số đánh giá (có thể là số nguyên hoặc số thực, tùy theo yêu cầu)

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

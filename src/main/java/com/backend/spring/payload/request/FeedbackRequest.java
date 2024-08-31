package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackRequest {
    private Integer feedBackId;

    @NotBlank(message = "Tên người gửi không được để trống!")
    private String name; // Tên người gửi phản hồi

    @NotBlank(message = "Email người gửi không được để trống!")
    private String email; // Địa chỉ email của người gửi phản hồi

    @NotBlank(message = "Nội dung đánh giá không được để trống!")
    private String review; // Nội dung đánh giá hoặc ý kiến

    @NotNull(message = "Chỉ số đánh giá không được để trống!")
    private Integer rating;  // Điểm số đánh giá (có thể là số nguyên hoặc số thực, tùy theo yêu cầu)
}

package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonContentRequest {
    private Integer contentId;

    @NotNull(message = "Id của bài học không được trống!")
    private Integer lessonId;

    @NotBlank(message = "Tiêu đề bài học không được bỏ trống!")
    private String title;

    @NotBlank(message = "Nội dung bài học không được để trống!")
    private String content;

    private Integer lessonContentStatus = 1;
}

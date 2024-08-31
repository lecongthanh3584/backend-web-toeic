package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserExamQuestionRequest {
    private Integer userExamQuestionId;

    @NotNull(message = "Id bài thi của user không được bỏ trống!")
    private Integer userExamId;

    @NotNull(message = "Id câu hỏi của bài thi không được bỏ trống!")
    private Integer examQuestionId;

    @NotBlank(message = "Đáp án trả lời không được bỏ trống!")
    private String selectedOption;

    @NotNull
    private Integer isCorrect;
}

package com.backend.spring.payload.request;

import com.backend.spring.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VocabularyQuestionRequest {
    private Integer questionId;

    @NotNull(message = "Id chủ đề không được bỏ trống!")
    private Integer topicId;

    @NotBlank(message = "Nội dung câu hỏi không được bỏ trống!")
    private String questionContent;

    @NotBlank(message = "Đáp án A không được bỏ trống!")
    private String optionA;

    @NotBlank(message = "Đáp án B không được bỏ trống!")
    private String optionB;

    @NotBlank(message = "Đáp án C không được bỏ trống!")
    private String optionC;

    @NotBlank(message = "Đáp án D không được bỏ trống!")
    private String optionD;

    @NotBlank(message = "Đáp án đúng không được bỏ trống!")
    private String correctOption;

    @NotBlank(message = "Giải thích đáp án không được bỏ trống!")
    private String questionExplanation;

    private Integer questionStatus = EStatus.DISABLE.getValue();
}

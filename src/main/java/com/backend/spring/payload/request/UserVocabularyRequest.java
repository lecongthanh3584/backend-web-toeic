package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserVocabularyRequest {
    private Long userExamId;

    @NotNull(message = "Id của người dùng không được bỏ trống!")
    private Integer userId;

    @NotNull(message = "Id của từ vựng không được bỏ trống!")
    private Integer vocabularyId;
}


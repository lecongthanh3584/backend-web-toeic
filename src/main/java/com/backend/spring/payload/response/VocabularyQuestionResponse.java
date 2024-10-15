package com.backend.spring.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyQuestionResponse {
    private Integer questionId;

    private String questionContent;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private String correctOption;

    private String questionExplanation;

    private Integer questionStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

package com.backend.spring.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ExamQuestionResponse {
    private Integer examQuestionId;

    private String questionContent;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private String correctOption;

    private String questionType;

    private String questionImage;

    private String questionScript;

    private String questionAudio;

    private String questionExplanation;

    private String questionPassage;

    private Integer orderNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer questionPart;
}

package com.backend.spring.payload.response;

import com.backend.spring.entities.QuestionGroup;
import com.backend.spring.entities.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private Integer questionId;

    private String questionContent;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private String correctOption;

    private String questionType;

    private String questionImage;

    private String questionScript;

    private String questionExplanation;

    private String questionAudio;

    private String questionPassage;

    private String questionText;

    private String suggestedAnswer;

    private Integer questionStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Section section;

    private QuestionGroup questionGroup;
}

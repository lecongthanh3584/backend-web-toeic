package com.backend.spring.payload.response;

import com.backend.spring.entity.Grammar;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrammarQuestionResponse {
    private Integer questionId;

    private Grammar grammar;

    private String questionContent;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private String correctOption;

    private String questionExplanation;

    private Integer questionStatus = 1;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

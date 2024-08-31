package com.backend.spring.mapper;

import com.backend.spring.entity.ExamQuestion;
import com.backend.spring.payload.response.ExamQuestionResponse;

public class ExamQuestionMapper {
    public static ExamQuestionResponse mapFromEntityToResponse(ExamQuestion examQuestion) {
        if(examQuestion == null) {
            return null;
        }

        return new ExamQuestionResponse(
                examQuestion.getExamQuestionId(),
                examQuestion.getQuestionContent(),
                examQuestion.getOptionA(),
                examQuestion.getOptionB(),
                examQuestion.getOptionC(),
                examQuestion.getOptionD(),
                examQuestion.getCorrectOption(),
                examQuestion.getQuestionContent(),
                examQuestion.getQuestionImage(),
                examQuestion.getQuestionScript(),
                examQuestion.getQuestionAudio(),
                examQuestion.getQuestionExplanation(),
                examQuestion.getQuestionPassage(),
                examQuestion.getOrderNumber(),
                examQuestion.getCreatedAt(),
                examQuestion.getUpdatedAt(),
                examQuestion.getQuestionPart()
        );

    }
}

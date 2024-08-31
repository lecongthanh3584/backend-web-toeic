package com.backend.spring.mapper;

import com.backend.spring.entity.Question;
import com.backend.spring.payload.response.QuestionResponse;

public class QuestionMapper {
    public static QuestionResponse mapFromEntityToResponse(Question question) {
        if(question == null) {
            return null;
        }

        return new QuestionResponse(
                question.getQuestionId(),
                question.getQuestionContent(),
                question.getOptionA(),
                question.getOptionB(),
                question.getOptionC(),
                question.getOptionD(),
                question.getCorrectOption(),
                question.getQuestionType(),
                question.getQuestionImage(),
                question.getQuestionScript(),
                question.getQuestionExplanation(),
                question.getQuestionAudio(),
                question.getQuestionPassage(),
                question.getQuestionText(),
                question.getSuggestedAnswer(),
                question.getQuestionStatus(),
                question.getCreatedAt(),
                question.getUpdatedAt(),
                question.getSection(),
                question.getQuestionGroup()
        );
    }
}

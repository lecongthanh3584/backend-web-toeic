package com.backend.spring.mapper;

import com.backend.spring.entities.VocabularyQuestion;
import com.backend.spring.payload.response.VocabularyQuestionResponse;

public class VocabularyQuestionMapper {
    public static VocabularyQuestionResponse mapFromEntityToResponse(VocabularyQuestion vocabularyQuestion) {
        if(vocabularyQuestion == null) {
            return null;
        }

        return new VocabularyQuestionResponse(
                vocabularyQuestion.getQuestionId(),
                vocabularyQuestion.getQuestionContent(),
                vocabularyQuestion.getOptionA(),
                vocabularyQuestion.getOptionB(),
                vocabularyQuestion.getOptionC(),
                vocabularyQuestion.getOptionD(),
                vocabularyQuestion.getCorrectOption(),
                vocabularyQuestion.getQuestionExplanation(),
                vocabularyQuestion.getQuestionStatus(),
                vocabularyQuestion.getCreatedAt(),
                vocabularyQuestion.getUpdatedAt()
        );
    }
}

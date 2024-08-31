package com.backend.spring.mapper;

import com.backend.spring.entity.GrammarQuestion;
import com.backend.spring.payload.response.GrammarQuestionResponse;

public class GrammarQuestionMapper {
    public static GrammarQuestionResponse mapFromEntityToResponse(GrammarQuestion grammarQuestion) {
        if(grammarQuestion == null) {
            return null;
        }

        return new GrammarQuestionResponse(
                grammarQuestion.getQuestionId(),
                grammarQuestion.getGrammar(),
                grammarQuestion.getQuestionContent(),
                grammarQuestion.getOptionA(),
                grammarQuestion.getOptionB(),
                grammarQuestion.getOptionC(),
                grammarQuestion.getOptionD(),
                grammarQuestion.getCorrectOption(),
                grammarQuestion.getQuestionExplanation(),
                grammarQuestion.getQuestionStatus(),
                grammarQuestion.getCreatedAt(),
                grammarQuestion.getUpdatedAt()
        );
    }
}

package com.backend.spring.mapper;

import com.backend.spring.entity.UserExamQuestion;
import com.backend.spring.payload.response.UserExamQuestionResponse;

public class UserExamQuestionMapper {
    public static UserExamQuestionResponse mapFromEntityToResponse(UserExamQuestion userExamQuestion) {
        if(userExamQuestion == null) {
            return null;
        }

        return new UserExamQuestionResponse(
                userExamQuestion.getUserExamQuestionId(),
                userExamQuestion.getSelectedOption(),
                userExamQuestion.getIsCorrect()
        );
    }
}

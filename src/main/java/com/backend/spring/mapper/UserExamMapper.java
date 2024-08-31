package com.backend.spring.mapper;

import com.backend.spring.entity.UserExam;
import com.backend.spring.payload.response.UserExamResponse;

public class UserExamMapper {
    public static UserExamResponse MapFromEntityToResponse(UserExam userExam) {
        if(userExam == null) {
            return null;
        }

        return new UserExamResponse(
                userExam.getUserExamId(),
                userExam.getCompletionTime(),
                userExam.getNumListeningCorrectAnswers(),
                userExam.getListeningScore(),
                userExam.getNumReadingCorrectAnswers(),
                userExam.getReadingScore(),
                userExam.getTotalScore(),
                userExam.getNumCorrectAnswers(),
                userExam.getNumWrongAnswers(),
                userExam.getNumSkippedQuestions(),
                userExam.getGoalScore(),
                userExam.getCreatedAt(),
                userExam.getUser(),
                userExam.getExam()
        );

    }
}

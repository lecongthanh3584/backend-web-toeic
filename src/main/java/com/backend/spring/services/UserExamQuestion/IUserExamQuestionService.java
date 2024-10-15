package com.backend.spring.services.UserExamQuestion;

import com.backend.spring.entities.UserExamQuestion;
import com.backend.spring.payload.request.UserExamQuestionRequest;

import java.util.List;

public interface IUserExamQuestionService {
    List<UserExamQuestion> getAll();
    void submitAllUserExamQuestions(List<UserExamQuestionRequest> userExamQuestionRequestList);
    List<UserExamQuestion> getQuestionsByUserExamId(Integer userExamId);
    List<UserExamQuestion> getUserExamQuestionsByUserId(Integer userId);

}

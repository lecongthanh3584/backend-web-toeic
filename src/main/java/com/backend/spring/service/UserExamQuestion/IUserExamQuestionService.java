package com.backend.spring.service.UserExamQuestion;

import com.backend.spring.entity.UserExam;
import com.backend.spring.entity.UserExamQuestion;
import com.backend.spring.payload.request.UserExamQuestionRequest;

import java.util.List;

public interface IUserExamQuestionService {
    List<UserExamQuestion> getAll();
    void submitAllUserExamQuestions(List<UserExamQuestionRequest> userExamQuestionRequestList);
    List<UserExamQuestion> getQuestionsByUserExamId(Integer userExamId);
    List<UserExamQuestion> getUserExamQuestionsByUserId(Integer userId);

}

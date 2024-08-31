package com.backend.spring.service.Question;

import com.backend.spring.entity.Question;
import com.backend.spring.payload.request.QuestionRequest;

import java.io.IOException;
import java.util.List;

public interface IQuestionService {
    Question createQuestion(QuestionRequest questionRequest) throws IOException;
    Question updateQuestion(QuestionRequest questionRequest) throws IOException;
    Question updateQuestionStatus(Integer id, Integer newStatus);
    List<Question> getAllQuestions();
    Question getQuestionById(Integer questionId);
    boolean deleteQuestion(Integer questionId) throws IOException;
    List<Question> getQuestionsBySectionId(Integer sectionId);
    List<Question> getQuestionsByGroupId(Integer groupId);
    List<Question> getQuestionsBySectionIdAndType(Integer sectionId, String questionType);
}

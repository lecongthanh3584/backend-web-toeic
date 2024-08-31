package com.backend.spring.service.ExamQuestion;

import com.backend.spring.entity.ExamQuestion;
import com.backend.spring.payload.request.ExamQuestionRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IExamQuestionService {
    boolean uploadExamQuestionsFromExcel(MultipartFile file, Integer examId) throws IOException;
    ExamQuestion createExamQuestion(ExamQuestionRequest examQuestionRequest) throws IOException;
    ExamQuestion updateExamQuestion(ExamQuestionRequest examQuestionRequest) throws IOException;
    ExamQuestion getExamQuestionById(Integer examQuestionId);
    boolean deleteExamQuestion(Integer examQuestionId);
    List<ExamQuestion> getAllExamQuestions();
    List<ExamQuestion> getExamQuestionsByExamId(Integer examId);
    boolean deleteAllExamQuestionsByExamId(Integer examId);
}

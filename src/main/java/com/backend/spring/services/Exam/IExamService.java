package com.backend.spring.services.Exam;

import com.backend.spring.entities.Exam;
import com.backend.spring.payload.request.ExamRequest;

import java.util.List;

public interface IExamService {
    List<Exam> getAllExams();
    Exam getExamById(Integer examId);
    Exam createExam(ExamRequest examRequest);
    Exam updateExam(ExamRequest examRequest);
    Exam updateExamStatus(Integer examId, Integer status);
    boolean deleteExam(Integer examId);
    List<Exam> getFullTests();
    List<Exam> getMiniTests();
    long countTotalExams();
    boolean isExamNameExists(String examName);
    boolean isExamNameExistsAndExamIdNot(String examName, Integer examId);
}

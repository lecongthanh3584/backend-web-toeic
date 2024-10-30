package com.backend.spring.services.Exam;

import com.backend.spring.entities.Exam;
import com.backend.spring.payload.request.ExamRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IExamService {
    Page<Exam> getFullTests(Integer pageNumber, String keyword, String... sortBys);
    Page<Exam> getMiniTests(Integer pageNumber, String keyword, String... sortBys);
    Exam getExamById(Integer examId);
    Exam createExam(ExamRequest examRequest);
    Exam updateExam(ExamRequest examRequest);
    Exam updateExamStatus(Integer examId, Integer status);
    boolean deleteExam(Integer examId);
    Page<Exam> getFullTestsEnable(Integer pageNumber, String keyword);
    Page<Exam> getMiniTestsEnable(Integer pageNumber, String keyword);
    long countTotalExams();
    boolean isExamNameExists(String examName);
    boolean isExamNameExistsAndExamIdNot(String examName, Integer examId);
}

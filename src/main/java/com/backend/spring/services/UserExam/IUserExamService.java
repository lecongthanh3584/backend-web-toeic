package com.backend.spring.services.UserExam;

import com.backend.spring.entities.UserExam;
import com.backend.spring.payload.request.UserExamRequest;
import com.backend.spring.payload.response.DailyExamCountResponse;
import com.backend.spring.payload.response.ExamAttemptResponse;

import java.util.List;

public interface IUserExamService {
    List<UserExam> getAllUserExams();
    UserExam getUserExamById(Integer id);
    UserExam createUserExam(UserExamRequest userExamRequest);
    List<UserExam> getUserExamsByExamIdAndUserId(Integer examId, Integer userId);
    boolean hasUserExamsByExamIdAndUserId(Integer examId, Integer userId);
    Long getTotalCompletionTimeByUserId(Integer userId);
    List<ExamAttemptResponse> getNumberAttemptForEachExam();
    List<DailyExamCountResponse> getDailyExamCounts();
    List<UserExam> getUserExamsByUserId(Integer userId);

}

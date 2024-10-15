package com.backend.spring.services.Test;

import com.backend.spring.entities.Question;
import com.backend.spring.entities.Test;
import com.backend.spring.payload.request.TestRequest;

import java.util.List;
import java.util.Set;

public interface ITestService {
    List<Test> getAllTests();
    Test getTestById(Integer testId);
    Test createTest(TestRequest testRequest);
    boolean deleteTest(Integer id);
    Test updateTest(TestRequest testRequest);
    Test updateTestStatus(Integer testId, Integer newStatus);
    Test updateTestParticipants(Integer testId, Integer participants);
    List<Test> getTestsBySectionId(Integer sectionId);
    Test updateQuestionsToTest(Integer testId, List<Integer> questionIds);
    Set<Question> getQuestionsByTestId(Integer testId);
    long countQuestionUsage(Integer questionId);
    String getTestNameByTestId(Integer testId);


}

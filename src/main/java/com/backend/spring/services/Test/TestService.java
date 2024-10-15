package com.backend.spring.services.Test;

import com.backend.spring.enums.EStatus;
import com.backend.spring.entities.Question;
import com.backend.spring.entities.Test;
import com.backend.spring.entities.Section;
import com.backend.spring.payload.request.TestRequest;
import com.backend.spring.repositories.QuestionRepository;
import com.backend.spring.repositories.TestRepository;
import com.backend.spring.repositories.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TestService implements ITestService {

    private final TestRepository testRepository;

    private final SectionRepository sectionRepository;

    private final QuestionRepository questionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Test getTestById(Integer testId) {
        return testRepository.findById(testId).orElse(null);
    }

    @Override
    public Test createTest(TestRequest testRequest) {
        Optional<Section> sectionOptional = sectionRepository.findById(testRequest.getSectionId());
        if (sectionOptional.isPresent()) {
            Test test = new Test();

            test.setSection(sectionOptional.get());
            test.setTestName(testRequest.getTestName());
            test.setTestParticipants(testRequest.getTestParticipants());
            test.setTestStatus(EStatus.ENABLE.getValue());
            test.setCreatedAt(LocalDateTime.now());
            test.setUpdatedAt(LocalDateTime.now());

            return testRepository.save(test);
        }

        return null;
    }

    @Override
    public boolean deleteTest(Integer id) {
        Optional<Test> testOptional = testRepository.findById(id);
        if(testOptional.isEmpty()) {
            return false;
        }

        testRepository.deleteById(id);
        return true;
    }

    @Override
    public Test updateTest(TestRequest testRequest) {
        Optional<Test> testOptional = testRepository.findById(testRequest.getTestId());
        Optional<Section> sectionOptional = sectionRepository.findById(testRequest.getSectionId());

        if (testOptional.isPresent() && sectionOptional.isPresent()) {
            Test testUpdate = testOptional.get();

            testUpdate.setSection(sectionOptional.get());
            testUpdate.setTestName(testRequest.getTestName());
            testUpdate.setTestParticipants(testRequest.getTestParticipants());
            testUpdate.setTestStatus(testRequest.getTestStatus());
            testUpdate.setUpdatedAt(LocalDateTime.now());

            return testRepository.save(testUpdate);
        }

        return null;
    }

    @Override
    public Test updateTestStatus(Integer testId, Integer newStatus) {
        Optional<Test> testOptional = testRepository.findById(testId);
        if(testOptional.isEmpty()) {
            return null;
        }

        Test testUpdate = testOptional.get();

        if(newStatus.equals(EStatus.DISABLE.getValue())) {
            testUpdate.setTestStatus(newStatus);
        } else if(newStatus.equals(EStatus.ENABLE.getValue())) {
            testUpdate.setTestStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Invalid status value!");
        }

        testUpdate.setUpdatedAt(LocalDateTime.now());

        return testRepository.save(testUpdate);
    }

    @Override
    public Test updateTestParticipants(Integer testId, Integer participants) {
        Optional<Test> testOptional = testRepository.findById(testId);

        if (testOptional.isPresent()) {
            Test testUpdate = testOptional.get();

            testUpdate.setTestParticipants(participants);
            testUpdate.setUpdatedAt(LocalDateTime.now());

            return testRepository.save(testUpdate);
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Test> getTestsBySectionId(Integer sectionId) {
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);

        if (sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            return testRepository.findBySection(section);
        }

        return Collections.emptyList();
    }

    @Override //Bảng Test-Question là có mối quan hệ nhiều nhiều, và đây là cách để thêm dữ liệu vào bảng test_question
    public Test updateQuestionsToTest(Integer testId, List<Integer> questionIds) {
        Optional<Test> testOptional = testRepository.findById(testId);

        if (testOptional.isPresent()) {
            Test testUpdate = testOptional.get();

            // Get new questions
            List<Question> newQuestions = questionRepository.findAllById(questionIds);
            // Clear existing questions and add new questions (Hash set)
            testUpdate.getQuestions().clear();
            testUpdate.getQuestions().addAll(newQuestions);

            return testRepository.save(testUpdate);
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Question> getQuestionsByTestId(Integer testId) {
        Optional<Test> testOptional = testRepository.findById(testId);

        if (testOptional.isPresent()) {
            Test test = testOptional.get();

            return test.getQuestions();
        }

        return Collections.emptySet();
    }

    @Override
    @Transactional(readOnly = true)
    public long countQuestionUsage(Integer questionId) {
        long count = 0;
        for (Test test : testRepository.findAll()) {
            if (test.getQuestions().stream().anyMatch(question -> question.getQuestionId().equals(questionId))) {
                count++;
            }
        }
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public String getTestNameByTestId(Integer testId) {
        Optional<Test> testOptional = testRepository.findById(testId);
        if(testOptional.isEmpty()) {
            return null;
        }

        return testOptional.get().getTestName();
    }

}

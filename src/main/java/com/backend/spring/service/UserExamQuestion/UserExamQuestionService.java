package com.backend.spring.service.UserExamQuestion;

import com.backend.spring.entity.*;
import com.backend.spring.payload.request.UserExamQuestionRequest;
import com.backend.spring.repository.UserExamQuestionRepository;
import com.backend.spring.repository.ExamQuestionRepository;
import com.backend.spring.repository.UserExamRepository;
import com.backend.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserExamQuestionService implements IUserExamQuestionService {

    @Autowired
    private UserExamQuestionRepository userExamQuestionRepository;

    @Autowired
    private UserExamRepository userExamRepository;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserExamQuestion> getAll() {
        return userExamQuestionRepository.findAll();
    }

//  Vòng lặp thêm từng câu hỏi
    @Override
    public void submitAllUserExamQuestions(List<UserExamQuestionRequest> userExamQuestionRequestList) {
        Optional<UserExam> userExamOptional = null;
        Optional<ExamQuestion> examQuestionOptional = null;

        for (UserExamQuestionRequest userExamQuestionRequest : userExamQuestionRequestList) {
            userExamOptional = userExamRepository.findById(userExamQuestionRequest.getUserExamId());
            examQuestionOptional = examQuestionRepository.findById(userExamQuestionRequest.getExamQuestionId());

            if (userExamOptional.isPresent() && examQuestionOptional.isPresent()) {
                // Thêm examId vào ExamResult
                UserExamQuestion userExamQuestion = new UserExamQuestion();

                userExamQuestion.setUserExam(userExamOptional.get());
                userExamQuestion.setExamQuestion(examQuestionOptional.get());
                userExamQuestion.setSelectedOption(userExamQuestionRequest.getSelectedOption());
                userExamQuestion.setIsCorrect(userExamQuestionRequest.getIsCorrect());

                userExamQuestionRepository.save(userExamQuestion);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserExamQuestion> getQuestionsByUserExamId(Integer userExamId) {
        Optional<UserExam> userExamOptional = userExamRepository.findById(userExamId);

        if (userExamOptional.isPresent()) {
            UserExam userExam = userExamOptional.get();

            return userExamQuestionRepository.findByUserExam(userExam);
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserExamQuestion> getUserExamQuestionsByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()) {
            return userExamQuestionRepository.getUserExamQuestionByUserId(userId);
        }

        return Collections.emptyList();
    }

}

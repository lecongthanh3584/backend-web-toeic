package com.backend.spring.services.UserExamQuestion;

import com.backend.spring.entities.*;
import com.backend.spring.payload.request.UserExamQuestionRequest;
import com.backend.spring.repositories.UserExamQuestionRepository;
import com.backend.spring.repositories.ExamQuestionRepository;
import com.backend.spring.repositories.UserExamRepository;
import com.backend.spring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserExamQuestionService implements IUserExamQuestionService {

    private final UserExamQuestionRepository userExamQuestionRepository;

    private final UserExamRepository userExamRepository;

    private final ExamQuestionRepository examQuestionRepository;

    private final UserRepository userRepository;

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

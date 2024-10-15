package com.backend.spring.services.UserExam;

import com.backend.spring.enums.EExamType;
import com.backend.spring.payload.response.DailyExamCountResponse;
import com.backend.spring.payload.response.ExamAttemptResponse;
import com.backend.spring.entities.*;
import com.backend.spring.payload.request.UserExamRequest;
import com.backend.spring.repositories.ExamRepository;
import com.backend.spring.repositories.UserExamRepository;
import com.backend.spring.repositories.UserRepository;
import com.backend.spring.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserExamService implements IUserExamService {

    private final UserExamRepository userExamRepository;

    private final UserRepository userRepository;

    private final ExamRepository examRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserExam> getAllUserExams() {
        return userExamRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserExam getUserExamById(Integer id) {
        return userExamRepository.findById(id).orElse(null);
    }

    @Override
    public UserExam createUserExam(UserExamRequest userExamRequest) {
        User userLogin = UserUtil.getDataUserLogin();
        Optional<Exam> examOptional = examRepository.findById(userExamRequest.getExamId());

        if (userLogin != null && examOptional.isPresent()) {

            UserExam userExam = new UserExam();

            userExam.setUser(userLogin);
            userExam.setExam(examOptional.get());
            userExam.setCompletionTime(userExamRequest.getCompletionTime());
            userExam.setNumListeningCorrectAnswers(userExamRequest.getNumListeningCorrectAnswers());
            userExam.setListeningScore(userExamRequest.getListeningScore());
            userExam.setNumReadingCorrectAnswers(userExamRequest.getNumReadingCorrectAnswers());
            userExam.setReadingScore(userExamRequest.getReadingScore());
            userExam.setTotalScore(userExamRequest.getTotalScore());

            userExam.setNumCorrectAnswers(userExamRequest.getNumCorrectAnswers());
            userExam.setNumWrongAnswers(userExamRequest.getNumWrongAnswers());
            userExam.setNumSkippedQuestions(userExamRequest.getNumSkippedQuestions());
            userExam.setGoalScore(userExamRequest.getGoalScore());
            userExam.setCreatedAt(LocalDateTime.now());

            return userExamRepository.save(userExam);
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserExam> getUserExamsByExamIdAndUserId(Integer examId, Integer userId) {
        Optional<Exam> examOptional = examRepository.findById(examId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (examOptional.isPresent() && userOptional.isPresent()) {
            Exam exam = examOptional.get();
            User user = userOptional.get();

            return userExamRepository.findByExamAndUser(exam, user);
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserExamsByExamIdAndUserId(Integer examId, Integer userId) {
        Optional<Exam> examOptional = examRepository.findById(examId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (examOptional.isPresent() && userOptional.isPresent()) {
            Exam exam = examOptional.get();
            User user = userOptional.get();

            return userExamRepository.existsByExamAndUser(exam, user);
        }

        return false; // Trả về false
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalCompletionTimeByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            return 0L;
        }

        //Có thể dùng câu lệnh sql để truy vấn, nhưng đây là dùng cách khác
        List<UserExam> userExams = userExamRepository.findByUser(userOptional.get());
        long totalSeconds = userExams.stream().map(UserExam::getCompletionTime).reduce(0, (a, b) -> a + b);

        return totalSeconds;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamAttemptResponse> getNumberAttemptForEachExam() {
        return userExamRepository.getNumberAttemptForEachExam();  //Chỉ tính các bài thi full-Test
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyExamCountResponse> getDailyExamCounts() {
        //Cách 1
//        List<UserExam> userExams = userExamRepository.findAll();
//
//        Map<String, Integer> dailyExamCounts = new HashMap<>();
//
//        for (UserExam userExam : userExams) {
//            // Lấy ngày của createdAt
//            LocalDate examDate = userExam.getCreatedAt().toLocalDate();
//
//            // Chuyển định dạng ngày thành chuỗi
//            String formattedDate = examDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
//
//            dailyExamCounts.put(formattedDate, dailyExamCounts.getOrDefault(formattedDate, 0) + 1);
//        }
//        // Sắp xếp theo chữ cái ngày
//
//        return new TreeMap<>(dailyExamCounts);

        //Cách 2

        return userExamRepository.getDailyExamCount();  //Chỉ tính các bài thi Full-Test
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserExam> getUserExamsByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Lấy danh sách bài thi của người dùng và lọc chỉ lấy những bài thi có examType là 1 (FULL TEST)
            return userExamRepository.findByUser(user)
                    .stream()
                    .filter(userExam -> userExam.getExam().getExamType().equals(EExamType.FULL_TEST.getValue())) //Lấy ra những bài thi nào là full-test
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

}


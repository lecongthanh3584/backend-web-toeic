package com.backend.spring.controller;

import com.backend.spring.mapper.UserExamQuestionMapper;
import com.backend.spring.entity.UserExamQuestion;
import com.backend.spring.payload.request.UserExamQuestionRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.UserExamQuestionResponse;
import com.backend.spring.service.UserExamQuestion.IUserExamQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class UserExamQuestionController {

    @Autowired
    private IUserExamQuestionService iUserExamQuestionService;

    // Lấy tất cả kết quả
    @GetMapping("/admin/user-exam-questions/get-all")
    public ResponseEntity<List<UserExamQuestionResponse>> getAll() {
        List<UserExamQuestionResponse> results = iUserExamQuestionService.getAll().stream().map(
                UserExamQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    // Nộp bài
    @PostMapping("/user/user-exam-questions/submit-all")
    public ResponseEntity<MessageResponse> submitAllUserExamQuestions(@RequestBody List<UserExamQuestionRequest> userExamQuestionRequestList) {
        iUserExamQuestionService.submitAllUserExamQuestions(userExamQuestionRequestList);
        return ResponseEntity.ok(new MessageResponse("Nộp bài thành công"));
    }

    // Lấy danh sách câu hỏi từ user_exam_id
    @GetMapping("/user/user-exam-questions/get-by-user-exam/{userExamId}")
    public ResponseEntity<List<UserExamQuestionResponse>> getQuestionsByUserExamId(@PathVariable Integer userExamId) {
        List<UserExamQuestionResponse> userExamQuestionsList = iUserExamQuestionService.getQuestionsByUserExamId(userExamId).stream().map(
                UserExamQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!userExamQuestionsList.isEmpty()) {
            return new ResponseEntity<>(userExamQuestionsList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(userExamQuestionsList, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/user-exam-questions/get-by-user-exam/{userExamId}/grouped")
    public ResponseEntity<Map<String, List<UserExamQuestion>>> getQuestionsByUserExamIdGroupedByType(@PathVariable Integer userExamId) {
        List<UserExamQuestion> userExamQuestionsList = iUserExamQuestionService.getQuestionsByUserExamId(userExamId);

        if (!userExamQuestionsList.isEmpty()) {
            // Tạo một Map để nhóm câu hỏi theo questionType
            Map<String, List<UserExamQuestion>> groupedQuestions = new HashMap<>();

            for (UserExamQuestion userExamQuestion : userExamQuestionsList) {

                String questionType = userExamQuestion.getExamQuestion().getQuestionType();
                if (!groupedQuestions.containsKey(questionType)) {
                    groupedQuestions.put(questionType, new ArrayList<>());
                }

                groupedQuestions.get(questionType).add(userExamQuestion);
            }

            // Sắp xếp Map theo questionType
            Map<String, List<UserExamQuestion>> sortedGroupedQuestions = groupedQuestions
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            return new ResponseEntity<>(sortedGroupedQuestions, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//  Thống kê độ chính xác từng phân loại câu hỏi
    @GetMapping("/user/user-exam-questions/accuracy-by-part/{questionPart}/user/{userId}")
    public ResponseEntity<Map<String, Double>> getAccuracyByQuestionTypeForUser(
            @PathVariable("questionPart") int questionPart,
            @PathVariable("userId") Integer userId)
    {

        List<UserExamQuestion> userExamQuestionsList = iUserExamQuestionService.getUserExamQuestionsByUserId(userId);

        if (!userExamQuestionsList.isEmpty()) {
            // Lọc ra các câu hỏi thuộc questionPart và có isCorrect != null

            List<UserExamQuestion> filteredQuestions = userExamQuestionsList.stream()
                    .filter(q -> q.getExamQuestion().getQuestionPart() == questionPart && q.getIsCorrect() != null)
                    .collect(Collectors.toList());

            // Tạo một Map để lưu độ chính xác từng loại câu hỏi
            Map<String, Double> accuracyByType = new HashMap<>();
            // Tính số câu đúng và tổng số câu cho từng loại câu hỏi
            for (UserExamQuestion userExamQuestion : filteredQuestions) {
                String questionType = userExamQuestion.getExamQuestion().getQuestionType();
                accuracyByType.putIfAbsent(questionType, 0.0);
                double correctCount = accuracyByType.get(questionType);
                if (userExamQuestion.getIsCorrect() == 1) {
                    accuracyByType.put(questionType, correctCount + 1);
                }
            }

            // Tính độ chính xác cho từng loại câu hỏi
            for (Map.Entry<String, Double> entry : accuracyByType.entrySet()) {
                double correctCount = entry.getValue();
                double totalCount = filteredQuestions.stream()
                        .filter(q -> q.getExamQuestion().getQuestionType().equals(entry.getKey()))
                        .count();
                double accuracy = (totalCount != 0) ? (correctCount / totalCount) * 100 : 0;

                // Làm tròn giá trị về 2 chữ số thập phân
                accuracy = Math.round(accuracy * 100.0) / 100.0;
                accuracyByType.put(entry.getKey(), accuracy);
            }

            return new ResponseEntity<>(accuracyByType, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

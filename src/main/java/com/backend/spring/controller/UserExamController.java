package com.backend.spring.controller;

import com.backend.spring.payload.response.DailyExamCountResponse;
import com.backend.spring.payload.response.ExamAttemptResponse;
import com.backend.spring.mapper.UserExamMapper;
import com.backend.spring.entity.UserExam;
import com.backend.spring.payload.request.UserExamRequest;
import com.backend.spring.payload.response.UserExamResponse;
import com.backend.spring.service.UserExam.IUserExamService;
import jakarta.validation.Valid;
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
public class UserExamController {

    @Autowired
    private IUserExamService iUserExamService;

    @GetMapping("/admin/user-exam/get-all")
    public ResponseEntity<List<UserExamResponse>> getAllUserExams() {
        List<UserExamResponse> userExamList = iUserExamService.getAllUserExams().stream().map(
                UserExamMapper::MapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(userExamList, HttpStatus.OK);
    }

    @GetMapping("/admin/user-exam/get-by-id/{id}")
    public ResponseEntity<UserExamResponse> getUserExamById(@PathVariable Integer id) {
        UserExamResponse userExam = UserExamMapper.MapFromEntityToResponse(iUserExamService.getUserExamById(id));

        if (userExam != null) {
            return new ResponseEntity<>(userExam, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user/user-exam/create")
    public ResponseEntity<?> createUserExam(@RequestBody @Valid UserExamRequest userExamRequest) {
        UserExam createdUserExam = iUserExamService.createUserExam(userExamRequest);

        if(createdUserExam != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userExamId", createdUserExam.getUserExamId());
            response.put("message", "Thêm kết quả bài thi của người dùng thành công!");
            return ResponseEntity.ok(response);

        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Thêm kết quả bài thi của người dùng thất bại!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/user-exam/get-user-exams/{examId}/{userId}")
    public ResponseEntity<?> getUserExamsByExamIdAndUserId(@PathVariable Integer examId, @PathVariable Integer userId ) {
        List<UserExamResponse> userExams = iUserExamService.getUserExamsByExamIdAndUserId(examId, userId).stream().map(
                UserExamMapper::MapFromEntityToResponse
        ).collect(Collectors.toList());

        // Sắp xếp danh sách userExams theo createdAt (ngày tạo) giảm dần (từ mới đến cũ)
        userExams.sort(Comparator.comparing(UserExamResponse::getCreatedAt).reversed());

        return ResponseEntity.ok(userExams);
    }

    @GetMapping("/user/user-exam/has-user-exams/{examId}/{userId}")
    public ResponseEntity<?> hasUserExamsByExamIdAndUserId(@PathVariable Integer examId, @PathVariable Integer userId) {
        boolean hasUserExams = iUserExamService.hasUserExamsByExamIdAndUserId(examId, userId);

        if (hasUserExams) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("hasUserExams", hasUserExams);
            response.put("message", "Người dùng đã tham gia bài thi này!");
            return ResponseEntity.ok(response);

        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Người dùng chưa tham gia bài thi này, có thể thi!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Thống kê bài thi có điểm cao nhất cho mỗi người dùng
    @GetMapping("/user/user-exam/get-max-scores-by-userId/{userId}")
    public ResponseEntity<Map<Integer, UserExamResponse>> getAllMaxScoresByUserId(@PathVariable Integer userId) {
        List<UserExam> userExams = iUserExamService.getUserExamsByUserId(userId); // Lấy tất cả bài thi FULLTEST

        Map<Integer, UserExamResponse> maxScoresByExamId = new LinkedHashMap<>();

        //Duyệt qua danh sách các bài thi và chỉ lấy bài thi có điểm cao nhất
        for (UserExam userExam : userExams) {
            int examId = userExam.getExam().getExamId();

            if(maxScoresByExamId.containsKey(examId) && userExam.getTotalScore() <= maxScoresByExamId.get(examId).getTotalScore()) {
                continue;
            }

            maxScoresByExamId.put(examId, UserExamMapper.MapFromEntityToResponse(userExam));
        }

        return new ResponseEntity<>(maxScoresByExamId, HttpStatus.OK);
    }

    // Thống kê điểm cao nhất hằng ngày cho mỗi người dùng
    @GetMapping("/user/user-exam/get-daily-highest-score-by-userId/{userId}")
    public ResponseEntity<Map<String, UserExamResponse>> getDailyHighestScoreByUserId(@PathVariable Integer userId) {
        List<UserExam> userExams = iUserExamService.getUserExamsByUserId(userId); // Lấy tất cả bài thi của người dùng cụ thể
        Map<String, UserExamResponse> maxScoresByDate = new LinkedHashMap<>();

        // Duyệt qua danh sách bài thi và chọn bài thi có điểm cao nhất cho mỗi ngày
        for (UserExam userExam : userExams) {
            String createdAt = userExam.getCreatedAt().toLocalDate().toString();
            if(maxScoresByDate.containsKey(createdAt) && userExam.getTotalScore() <= maxScoresByDate.get(createdAt).getTotalScore()) {
                continue;
            }

            maxScoresByDate.put(createdAt, UserExamMapper.MapFromEntityToResponse(userExam));
        }
        return new ResponseEntity<>(maxScoresByDate, HttpStatus.OK);
    }

//  Tổng thời gian luyện tập
    @GetMapping("/user/user-exam/get-total-completion-time-by-userId/{userId}")
    public ResponseEntity<Long> getTotalCompletionTimeByUserId(@PathVariable Integer userId) {
        Long totalCompletionTime = iUserExamService.getTotalCompletionTimeByUserId(userId);
        // Trả về tổng thời gian luyện tập dưới dạng số giây
        return ResponseEntity.ok(totalCompletionTime);
    }

//  Thống kê tổng số lượt thi của từng bài thi (ADMIN)
    @GetMapping("/admin/user-exam/get-number-attempt-for-each-exam")
    public ResponseEntity<?> getNumberAttemptForEachExam() {
        try {
            List<ExamAttemptResponse> totalExamCountsByExamName = iUserExamService.getNumberAttemptForEachExam();
            return ResponseEntity.ok(totalExamCountsByExamName);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi truy vấn tổng số người thi cho từng bài thi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

//  Tổng số lượt thi hằng ngày (ADMIN)
    @GetMapping("/admin/user-exam/get-daily-exam-counts")
    public ResponseEntity<?> getDailyExamCounts() {
        try {
            List<DailyExamCountResponse> dailyExamCounts = iUserExamService.getDailyExamCounts();
            return ResponseEntity.ok(dailyExamCounts);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi truy vấn tổng số lượt thi hằng ngày: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Tính độ dài (length) của danh sách bài thi của người dùng theo userId
    @GetMapping("/user/user-exam/get-quantity-exam-for-user/{userId}")
    public ResponseEntity<Integer> getUserExamsLengthByUserId(@PathVariable Integer userId) {
        List<UserExam> userExams = iUserExamService.getUserExamsByUserId(userId);

        if (!userExams.isEmpty()) {
            return ResponseEntity.ok(userExams.size());
        } else {
            return new ResponseEntity<>(0, HttpStatus.NOT_FOUND); // Không tìm thấy bài thi hoặc người dùng
        }
    }

}


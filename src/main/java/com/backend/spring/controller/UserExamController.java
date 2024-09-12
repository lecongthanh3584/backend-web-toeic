package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.payload.response.DailyExamCountResponse;
import com.backend.spring.payload.response.ExamAttemptResponse;
import com.backend.spring.mapper.UserExamMapper;
import com.backend.spring.entity.UserExam;
import com.backend.spring.payload.request.UserExamRequest;
import com.backend.spring.payload.response.UserExamResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.UserExam.IUserExamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@Validated
public class UserExamController {

    @Autowired
    private IUserExamService iUserExamService;

    @GetMapping("/admin/user-exam/get-all")
    public ResponseEntity<?> getAllUserExams() {
        List<UserExamResponse> userExamList = iUserExamService.getAllUserExams().stream().map(
                UserExamMapper::MapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserExam.GET_DATA_SUCCESS, userExamList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/user-exam/get-by-id/{id}")
    public ResponseEntity<?> getUserExamById(@PathVariable("id") @Min(1) Integer id) {
        UserExamResponse userExam = UserExamMapper.MapFromEntityToResponse(iUserExamService.getUserExamById(id));

        if (userExam != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserExam.GET_DATA_SUCCESS, userExam),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.UserExam.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user/user-exam/create")
    public ResponseEntity<?> createUserExam(@RequestBody @Valid UserExamRequest userExamRequest) {
        UserExam createdUserExam = iUserExamService.createUserExam(userExamRequest);

        if(createdUserExam != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.UserExam.CREATE_SUCCESS, createdUserExam.getUserExamId()),
                    HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.UserExam.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/user-exam/get-user-exams/{examId}/{userId}")
    public ResponseEntity<?> getUserExamsByExamIdAndUserId(@PathVariable Integer examId, @PathVariable Integer userId ) {
        List<UserExamResponse> userExams = iUserExamService.getUserExamsByExamIdAndUserId(examId, userId).stream().map(
                UserExamMapper::MapFromEntityToResponse
        ).collect(Collectors.toList());

        // Sắp xếp danh sách userExams theo createdAt (ngày tạo) giảm dần (từ mới đến cũ)
        userExams.sort(Comparator.comparing(UserExamResponse::getCreatedAt).reversed());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserExam.GET_DATA_SUCCESS, userExams),
                HttpStatus.OK);
    }

    @GetMapping("/user/user-exam/has-user-exams/{examId}/{userId}")
    public ResponseEntity<?> hasUserExamsByExamIdAndUserId(@PathVariable Integer examId, @PathVariable Integer userId) {
        boolean hasUserExams = iUserExamService.hasUserExamsByExamIdAndUserId(examId, userId);

        if (hasUserExams) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserExam.USER_JOINED, hasUserExams),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserExam.USER_NOT_JOINED, hasUserExams),
                    HttpStatus.OK);
        }
    }

    // Thống kê bài thi có điểm cao nhất cho mỗi người dùng
    @GetMapping("/user/user-exam/get-max-scores-by-userId/{userId}")
    public ResponseEntity<?> getAllMaxScoresByUserId(@PathVariable("userId") @Min(1) Integer userId) {
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

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserExam.GET_DATA_SUCCESS, maxScoresByExamId),
                HttpStatus.OK);
    }

    // Thống kê điểm cao nhất hằng ngày cho mỗi người dùng
    @GetMapping("/user/user-exam/get-daily-highest-score-by-userId/{userId}")
    public ResponseEntity<?> getDailyHighestScoreByUserId(@PathVariable Integer userId) {
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
        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserExam.GET_DATA_SUCCESS, maxScoresByDate),
                HttpStatus.OK);
    }

//  Tổng thời gian luyện tập
    @GetMapping("/user/user-exam/get-total-completion-time-by-userId/{userId}")
    public ResponseEntity<?> getTotalCompletionTimeByUserId(@PathVariable("userId") @Min(1) Integer userId) {
        Long totalCompletionTime = iUserExamService.getTotalCompletionTimeByUserId(userId);
        // Trả về tổng thời gian luyện tập dưới dạng số giây
        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserExam.GET_DATA_SUCCESS, totalCompletionTime),
                HttpStatus.OK);
    }

//  Thống kê tổng số lượt thi của từng bài thi (ADMIN)
    @GetMapping("/admin/user-exam/get-number-attempt-for-each-exam")
    public ResponseEntity<?> getNumberAttemptForEachExam() {
        try {
            List<ExamAttemptResponse> totalExamCountsByExamName = iUserExamService.getNumberAttemptForEachExam();
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserExam.GET_DATA_SUCCESS, totalExamCountsByExamName),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//  Tổng số lượt thi hằng ngày (ADMIN)
    @GetMapping("/admin/user-exam/get-daily-exam-counts")
    public ResponseEntity<?> getDailyExamCounts() {
        try {
            List<DailyExamCountResponse> dailyExamCounts = iUserExamService.getDailyExamCounts();
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserExam.GET_DATA_SUCCESS, dailyExamCounts),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tính độ dài (length) của danh sách bài thi của người dùng theo userId
    @GetMapping("/user/user-exam/get-quantity-exam-for-user/{userId}")
    public ResponseEntity<?> getUserExamsLengthByUserId(@PathVariable("userId") @Min(1) Integer userId) {
        List<UserExam> userExams = iUserExamService.getUserExamsByUserId(userId);

        if (!userExams.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserExam.GET_DATA_SUCCESS, userExams.size()),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.UserExam.DATA_NOT_FOUND, 0),
                    HttpStatus.NOT_FOUND);
        }
    }

}


package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.ExamMapper;
import com.backend.spring.entities.Exam;
import com.backend.spring.payload.request.ExamRequest;
import com.backend.spring.payload.response.ExamResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.Exam.IExamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class ExamController {

    private final IExamService iExamService;

    @GetMapping("/admin/exam/get-all")
    public ResponseEntity<?> getAllExams() {
        List<ExamResponse> examResponseList = iExamService.getAllExams().stream().map(
                ExamMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Exam.GET_DATA_SUCCESS, examResponseList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/exam/get-by-id/{id}")
    public ResponseEntity<?> getExamByExamId(@PathVariable("id") @Min(1) Integer examId) {
        ExamResponse examResponse = ExamMapper.mapFromEntityToResponse(iExamService.getExamById(examId));

        if (examResponse != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Exam.GET_DATA_SUCCESS, examResponse),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Exam.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/exam/create")
    public ResponseEntity<?> createExam(@RequestBody @Valid ExamRequest ExamRequest) {
        // Kiểm tra xem tên exam đã tồn tại chưa
        if (iExamService.isExamNameExists(ExamRequest.getExamName())) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Exam.EXAM_NAME_EXIST),
                    HttpStatus.BAD_REQUEST);
        }

        Exam createdExam = iExamService.createExam(ExamRequest);

        if(createdExam != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Exam.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Exam.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/admin/exam/update")
    public ResponseEntity<?> updateExam(@RequestBody @Valid ExamRequest examRequest) {
        // Kiểm tra trùng lặp tên exam (nếu tên đã thay đổi)
        if (iExamService.isExamNameExistsAndExamIdNot(examRequest.getExamName(), examRequest.getExamId())) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Exam.EXAM_NAME_EXIST),
                    HttpStatus.BAD_REQUEST);
        }

        Exam updatedExam = iExamService.updateExam(examRequest);

        if (updatedExam != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Exam.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Exam.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/exam/delete/{id}")
    public ResponseEntity<?> deleteExam(@PathVariable("id") @Min(1) Integer examId) {
        boolean result = iExamService.deleteExam(examId);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.Exam.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.Exam.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/admin/exam/update-status/{id}")
    public ResponseEntity<?> updateExamStatus(@PathVariable("id") @Min(1) Integer examId, @RequestBody Integer newStatus) {
        Exam examResult = iExamService.updateExamStatus(examId, newStatus);

        if(examResult != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Exam.UPDATE_STATUS_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Exam.UPDATE_STATUS_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/exam/get-all-mini-tests")
    public ResponseEntity<?> getMiniTests() {
        List<ExamResponse> miniTests = iExamService.getMiniTests().stream().map(
                ExamMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Exam.GET_DATA_SUCCESS, miniTests),
                HttpStatus.OK);
    }

//  Admin
    @GetMapping("/admin/exam/get-all-full-tests")
    public ResponseEntity<?> getFullTests() {
        List<ExamResponse> fullTests = iExamService.getFullTests().stream().map(
                ExamMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Exam.GET_DATA_SUCCESS, fullTests),
                HttpStatus.OK);
    }

    @GetMapping("/public/exam/get-full-tests/enable")
    public ResponseEntity<?> getEnableFullTests() {
        List<ExamResponse> fullTests = iExamService.getFullTests().stream().map(
                ExamMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các Exam có examStatus là 1 (kích hoạt)
        List<ExamResponse> filteredFullTests = fullTests.stream()
                .filter(exam -> exam.getExamStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredFullTests.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Exam.GET_DATA_SUCCESS, filteredFullTests),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Exam.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/exam/total")
    public ResponseEntity<?> countTotalExams() {
        long totalExams = iExamService.countTotalExams();

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Exam.GET_DATA_SUCCESS, totalExams),
                HttpStatus.OK);
    }

}

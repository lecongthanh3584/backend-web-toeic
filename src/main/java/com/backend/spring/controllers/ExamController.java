package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.elastics.services.Exam.IExamDocumentService;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.ExamMapper;
import com.backend.spring.entities.Exam;
import com.backend.spring.payload.request.ExamRequest;
import com.backend.spring.payload.response.ExamResponse;
import com.backend.spring.payload.response.main.PaginationData;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.Exam.IExamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    private final IExamDocumentService iExamDocumentService;

    //Admin
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
    public ResponseEntity<?> getMiniTests(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "created_at:desc") String... sortBys
    ) {
        Page<Exam> examPage = iExamService.getMiniTests(page, keyword, sortBys);

        PaginationData paginationData = PaginationData.builder().totalPage(examPage.getTotalPages()).totalElement(examPage.getTotalElements())
                .pageNumber(examPage.getPageable().getPageNumber()).pageSize(examPage.getPageable().getPageSize()).build();

        List<ExamResponse> examResponseList = examPage.getContent().stream().map(
                ExamMapper::mapFromEntityToResponse
        ).toList();

        if (!examResponseList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Exam.GET_DATA_SUCCESS,
                    paginationData, examResponseList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Exam.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/exam/get-all-full-tests")
    public ResponseEntity<?> getFullTests(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "created_at:desc") String... sortBys
    ) {
        Page<Exam> examPage = iExamService.getFullTests(page, keyword, sortBys);

        PaginationData paginationData = PaginationData.builder().totalPage(examPage.getTotalPages()).totalElement(examPage.getTotalElements())
                .pageNumber(examPage.getPageable().getPageNumber()).pageSize(examPage.getPageable().getPageSize()).build();

        List<ExamResponse> examResponseList = examPage.getContent().stream().map(
                ExamMapper::mapFromEntityToResponse
        ).toList();

        if (!examResponseList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Exam.GET_DATA_SUCCESS,
                    paginationData, examResponseList), HttpStatus.OK);
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

    //User
    @GetMapping("/public/exam/get-full-tests/enable")
    public ResponseEntity<?> getEnableFullTests(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        Page<Exam> fullTests = iExamService.getFullTestsEnable(page, keyword);

        PaginationData paginationData = PaginationData.builder().totalPage(fullTests.getTotalPages()).totalElement(fullTests.getTotalElements())
                .pageNumber(fullTests.getPageable().getPageNumber()).pageSize(fullTests.getPageable().getPageSize()).build();

        List<ExamResponse> examResponseList = fullTests.getContent().stream().map(
                ExamMapper::mapFromEntityToResponse
        ).toList();

        if (!examResponseList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Exam.GET_DATA_SUCCESS,
                    paginationData, examResponseList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Exam.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/exam/get-mini-tests/enable")
    public ResponseEntity<?> getEnableMiniTests(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        Page<Exam> fullTests = iExamService.getMiniTestsEnable(page, keyword);

        PaginationData paginationData = PaginationData.builder().totalPage(fullTests.getTotalPages()).totalElement(fullTests.getTotalElements())
                .pageNumber(fullTests.getPageable().getPageNumber()).pageSize(fullTests.getPageable().getPageSize()).build();

        List<ExamResponse> examResponseList = fullTests.getContent().stream().map(
                ExamMapper::mapFromEntityToResponse
        ).toList();

        if (!examResponseList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Exam.GET_DATA_SUCCESS,
                    paginationData, examResponseList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Exam.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/get-exam-document")
    public ResponseEntity<?> getExamDocument() {
        return new ResponseEntity<>(iExamDocumentService.getAllExamDocument(),HttpStatus.OK);
    }
}

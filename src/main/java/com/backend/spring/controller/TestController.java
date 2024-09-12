package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.QuestionMapper;
import com.backend.spring.mapper.TestMapper;
import com.backend.spring.entity.Test;
import com.backend.spring.payload.request.TestRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.QuestionResponse;
import com.backend.spring.payload.response.TestResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.Test.ITestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Validated
public class TestController {

    @Autowired
    private ITestService iTestService;

    @GetMapping("/admin/test/get-all")
    public ResponseEntity<?> getAllTests() {
        List<TestResponse> testList = iTestService.getAllTests().stream().map(
                TestMapper::MapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Test.GET_DATA_SUCCESS, testList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/test/get-by-id/{id}")
    public ResponseEntity<?> getTestById(@PathVariable("id") @Min(1) Integer testId) {
        TestResponse test = TestMapper.MapFromEntityToResponse(iTestService.getTestById(testId));

        if (test != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Test.GET_DATA_SUCCESS, test),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Test.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/test/create")
    public ResponseEntity<?> createTest(@RequestBody @Valid TestRequest testRequest) {
        Test createdTest = iTestService.createTest(testRequest);

        if (createdTest != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Test.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Test.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/test/update")
    public ResponseEntity<?> updateTest(@RequestBody @Valid TestRequest testRequest) {
        Test updatedTest = iTestService.updateTest(testRequest);

        if (updatedTest != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Test.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Test.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/test/delete/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iTestService.deleteTest(id);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.Test.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.Test.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/admin/test/update-status/{id}")
    public ResponseEntity<?> updateTestStatus(@PathVariable("id") @Min(1) Integer testId, @RequestBody Integer newStatus) {
        Test updateTest = iTestService.updateTestStatus(testId, newStatus);

        if(updateTest != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Test.UPDATE_STATUS_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Test.UPDATE_STATUS_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/test/update-participant/{id}")
    public ResponseEntity<?> updateTestParticipants(@PathVariable("id") @Min(1) Integer testId, @RequestBody Integer newParticipants) {
        Test testUpdate = iTestService.updateTestParticipants(testId, newParticipants);

        if(testUpdate != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Test.UPDATE_PARTICIPANT_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Test.UPDATE_PARTICIPANT_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    // Get tests by sectionId (Admin)
    @GetMapping("/admin/test/get-by-section/{sectionId}")
    public ResponseEntity<?> getTestsBySectionId(@PathVariable("sectionId") @Min(1) Integer sectionId) {
        List<TestResponse> testList = iTestService.getTestsBySectionId(sectionId).stream().map(
                TestMapper::MapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!testList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Test.GET_DATA_SUCCESS, testList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Test.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    // Get tests by sectionId (User)
    @GetMapping("/public/test/get-by-section/{sectionId}/enable")
    public ResponseEntity<?> getEnableTestsBySectionId(@PathVariable("sectionId") @Min(1) Integer sectionId) {
        List<TestResponse> testList = iTestService.getTestsBySectionId(sectionId).stream().map(
                TestMapper::MapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các Test có testStatus là 1
        List<TestResponse> filteredTests = testList.stream()
                .filter(test -> test.getTestStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredTests.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Test.GET_DATA_SUCCESS, filteredTests),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Test.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    /** Thêm câu hỏi vào test cụ thể **/
    @PutMapping("/admin/test/add-questions-to-test/{id}")
    public ResponseEntity<?> addQuestionsToTest(@PathVariable("id") @Min(1) Integer testId, @RequestBody List<Integer> questionIds) {
        Test updatedTest = iTestService.updateQuestionsToTest(testId, questionIds);

        if (updatedTest != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Test.UPDATE_QUESTION_TO_TEST_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Test.UPDATE_QUESTION_TO_TEST_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/test/get-test-name-by-id/{id}")
    public ResponseEntity<?> getTestNameByTestId(@PathVariable("id") @Min(1) Integer testId) {
        String testName = iTestService.getTestNameByTestId(testId);

        if (testName != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Test.GET_DATA_SUCCESS, testName),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Test.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/test/get-question-by-test/{testId}")
    public ResponseEntity<?> getQuestionsByTestId(@PathVariable("testId") @Min(1) Integer testId) {
        Set<QuestionResponse> questions = iTestService.getQuestionsByTestId(testId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toSet());

        if (!questions.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Test.GET_DATA_SUCCESS, questions),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Test.DATA_NOT_FOUND, questions),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/test/get-question-by-test/{testId}/enable")
    public ResponseEntity<?> getQuestionsEnableByTestId(@PathVariable("testId") @Min(1) Integer testId) {
        Set<QuestionResponse> questions = iTestService.getQuestionsByTestId(testId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toSet());

        Set<QuestionResponse> questionResponsesEnable = questions.stream().filter(
                item -> item.getQuestionStatus().equals(EStatus.ENABLE.getValue())
        ).collect(Collectors.toSet());

        if (!questions.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Test.GET_DATA_SUCCESS, questionResponsesEnable),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Test.DATA_NOT_FOUND, questionResponsesEnable),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/test/question-usage/{questionId}")
    public ResponseEntity<?> getQuestionUsageCount(@PathVariable("questionId") @Min(1) Integer questionId) {
        long count = iTestService.countQuestionUsage(questionId);

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Test.GET_DATA_SUCCESS, count),
                HttpStatus.OK);
    }


}

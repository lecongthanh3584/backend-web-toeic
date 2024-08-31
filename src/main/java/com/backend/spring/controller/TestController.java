package com.backend.spring.controller;

import com.backend.spring.enums.EStatus;
import com.backend.spring.mapper.QuestionMapper;
import com.backend.spring.mapper.TestMapper;
import com.backend.spring.entity.Test;
import com.backend.spring.payload.request.TestRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.QuestionResponse;
import com.backend.spring.payload.response.TestResponse;
import com.backend.spring.service.Test.ITestService;
import jakarta.validation.Valid;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class TestController {

    @Autowired
    private ITestService iTestService;

    @GetMapping("/admin/test/get-all")
    public ResponseEntity<List<TestResponse>> getAllTests() {
        List<TestResponse> testList = iTestService.getAllTests().stream().map(
                TestMapper::MapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(testList, HttpStatus.OK);
    }

    @GetMapping("/admin/test/get-by-id/{id}")
    public ResponseEntity<TestResponse> getTestById(@PathVariable("id") Integer testId) {
        TestResponse test = TestMapper.MapFromEntityToResponse(iTestService.getTestById(testId));

        if (test != null) {
            return new ResponseEntity<>(test, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/test/create")
    public ResponseEntity<MessageResponse> createTest(@RequestBody @Valid TestRequest testRequest) {
        Test createdTest = iTestService.createTest(testRequest);

        if (createdTest != null) {
            return ResponseEntity.ok(new MessageResponse("Thêm bài kiểm tra thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Thêm bài kiểm tra thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/test/update")
    public ResponseEntity<MessageResponse> updateTest(@RequestBody @Valid TestRequest testRequest) {
        Test updatedTest = iTestService.updateTest(testRequest);

        if (updatedTest != null) {
            return ResponseEntity.ok(new MessageResponse("Cập nhật bài kiểm tra thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật bài kiểm tra thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/test/delete/{id}")
    public ResponseEntity<MessageResponse> deleteTest(@PathVariable Integer id) {
        boolean result = iTestService.deleteTest(id);

        if(result) {
            return ResponseEntity.ok(new MessageResponse("Xóa bài kiểm tra thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Xoá bài kiểm tra thất bại!"), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/admin/test/update-status/{id}")
    public ResponseEntity<MessageResponse> updateTestStatus(@PathVariable("id") Integer testId, @RequestBody Integer newStatus) {
        Test updateTest = iTestService.updateTestStatus(testId, newStatus);

        if(updateTest != null) {
            return new ResponseEntity<>(new MessageResponse("Cập nhật status của bài test thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật status của bài test thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/test/update-participant/{id}")
    public ResponseEntity<MessageResponse> updateTestParticipants(@PathVariable("id") Integer testId, @RequestBody Integer newParticipants) {
        Test testUpdate = iTestService.updateTestParticipants(testId, newParticipants);

        if(testUpdate != null) {
            return new ResponseEntity<>(new MessageResponse("Cập nhật người tham gia test thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật người tham gia test thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    // Get tests by sectionId (Admin)
    @GetMapping("/admin/test/get-by-section/{sectionId}")
    public ResponseEntity<List<TestResponse>> getTestsBySectionId(@PathVariable Integer sectionId) {
        List<TestResponse> testList = iTestService.getTestsBySectionId(sectionId).stream().map(
                TestMapper::MapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!testList.isEmpty()) {
            return new ResponseEntity<>(testList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(testList, HttpStatus.NOT_FOUND);
        }
    }

    // Get tests by sectionId (Người dùng)
    @GetMapping("/public/test/get-by-section/{sectionId}/enable")
    public ResponseEntity<List<TestResponse>> getEnableTestsBySectionId(@PathVariable Integer sectionId) {
        List<TestResponse> testList = iTestService.getTestsBySectionId(sectionId).stream().map(
                TestMapper::MapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các Test có testStatus là 1
        List<TestResponse> filteredTests = testList.stream()
                .filter(test -> test.getTestStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredTests.isEmpty()) {
            return new ResponseEntity<>(filteredTests, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(filteredTests, HttpStatus.NOT_FOUND);
        }
    }

    /** Thêm câu hỏi vào test cụ thể **/
    @PutMapping("/admin/test/add-questions-to-test/{id}")
    public ResponseEntity<MessageResponse> addQuestionsToTest(@PathVariable("id") Integer testId, @RequestBody List<Integer> questionIds) {
        Test updatedTest = iTestService.updateQuestionsToTest(testId, questionIds);

        if (updatedTest != null) {
            return ResponseEntity.ok(new MessageResponse("Cập nhật câu hỏi bài kiểm tra thành công!"));
        } else {
           return new ResponseEntity<>(new MessageResponse("Cập nhật câu hỏi bài kiểm tra thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/test/get-test-name-by-id/{id}")
    public ResponseEntity<String> getTestNameByTestId(@PathVariable("id") Integer testId) {
        String testName = iTestService.getTestNameByTestId(testId);

        if (testName != null) {
            return new ResponseEntity<>(testName, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/test/get-question-by-test/{testId}")
    public ResponseEntity<Set<QuestionResponse>> getQuestionsByTestId(@PathVariable Integer testId) {
        Set<QuestionResponse> questions = iTestService.getQuestionsByTestId(testId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toSet());

        if (!questions.isEmpty()) {
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(questions, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/test/get-question-by-test/{testId}/enable")
    public ResponseEntity<Set<QuestionResponse>> getQuestionsEnableByTestId(@PathVariable Integer testId) {
        Set<QuestionResponse> questions = iTestService.getQuestionsByTestId(testId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toSet());

        Set<QuestionResponse> questionResponsesEnable = questions.stream().filter(
                item -> item.getQuestionStatus().equals(EStatus.ENABLE.getValue())
        ).collect(Collectors.toSet());

        if (!questions.isEmpty()) {
            return new ResponseEntity<>(questionResponsesEnable, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(questionResponsesEnable, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/test/question-usage/{questionId}")
    public ResponseEntity<Long> getQuestionUsageCount(@PathVariable Integer questionId) {
        long count = iTestService.countQuestionUsage(questionId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }


}

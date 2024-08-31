package com.backend.spring.controller;

import com.backend.spring.enums.EStatus;
import com.backend.spring.mapper.QuestionMapper;
import com.backend.spring.entity.Question;
import com.backend.spring.payload.request.QuestionRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.QuestionResponse;
import com.backend.spring.service.Question.IQuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class QuestionController {

    @Autowired
    private IQuestionService iQuestionService;

    @GetMapping("/admin/question/get-all")
    public ResponseEntity<List<QuestionResponse>> getAllQuestions() {
        List<QuestionResponse> questionList = iQuestionService.getAllQuestions().stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(questionList, HttpStatus.OK);
    }

    @GetMapping("/admin/question/get-by-id/{id}")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable Integer id) {
        QuestionResponse question = QuestionMapper.mapFromEntityToResponse(iQuestionService.getQuestionById(id));

        if (question != null) {
            return new ResponseEntity<>(question, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/question/get-question-by-section/{sectionId}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsBySectionId(@PathVariable Integer sectionId) {
        List<QuestionResponse> questionList = iQuestionService.getQuestionsBySectionId(sectionId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!questionList.isEmpty()) {
            return new ResponseEntity<>(questionList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(questionList, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/question/get-question-by-section/{sectionId}/enable")
    public ResponseEntity<List<QuestionResponse>> getQuestionsBySectionIdEnable(@PathVariable Integer sectionId) {
        List<QuestionResponse> questionList = iQuestionService.getQuestionsBySectionId(sectionId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        List<QuestionResponse> questionResponseEnableList = questionList.stream().filter(
                item -> item.getQuestionStatus().equals(EStatus.ENABLE.getValue())
        ).collect(Collectors.toList());

        if (!questionList.isEmpty()) {
            return new ResponseEntity<>(questionResponseEnableList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(questionResponseEnableList, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/question/get-question-by-group/{groupId}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByGroupId(@PathVariable Integer groupId) {
        List<QuestionResponse> questionList = iQuestionService.getQuestionsByGroupId(groupId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!questionList.isEmpty()) {
            return new ResponseEntity<>(questionList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(questionList, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/question/get-question-by-group/{groupId}/enable")
    public ResponseEntity<List<QuestionResponse>> getQuestionsEnableByGroupId(@PathVariable Integer groupId) {
        List<QuestionResponse> questionList = iQuestionService.getQuestionsByGroupId(groupId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        List<QuestionResponse> questionResponseEnableList = questionList.stream().filter(
                item -> item.getQuestionStatus().equals(EStatus.ENABLE.getValue())
        ).collect(Collectors.toList());

        if (!questionList.isEmpty()) {
            return new ResponseEntity<>(questionResponseEnableList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(questionResponseEnableList, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/question/create")
    public ResponseEntity<MessageResponse> createQuestion(@ModelAttribute @Valid QuestionRequest questionRequest) {
        try {
            Question createdQuestion = iQuestionService.createQuestion(questionRequest);

            if(createdQuestion != null) {
                return ResponseEntity.ok(new MessageResponse("Thêm câu hỏi thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Thêm câu hỏi thất bại!"), HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/admin/question/update")
    public ResponseEntity<MessageResponse> updateQuestion(@ModelAttribute @Valid QuestionRequest questionRequest) {
        try {
            Question updatedQuestion = iQuestionService.updateQuestion(questionRequest);

            if (updatedQuestion != null) {
                return ResponseEntity.ok(new MessageResponse("Cập nhật câu hỏi thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Cập nhật câu hỏi thất bại!"), HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi khi cập nhật: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/question/delete/{id}")
    public ResponseEntity<MessageResponse> deleteQuestion(@PathVariable Integer id) {
        try {
            boolean result = iQuestionService.deleteQuestion(id);

            if(result) {
                return ResponseEntity.ok(new MessageResponse("Xóa câu hỏi thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Xoá câu hỏi thất bại!"), HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi khi xoá: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/question/update-status/{id}")
    public ResponseEntity<MessageResponse> updateQuestionStatus(@PathVariable Integer id, @RequestBody Integer newStatus) {
        Question questionUpdate = iQuestionService.updateQuestionStatus(id, newStatus);

        if(questionUpdate != null) {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái câu hỏi thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái câu hỏi thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

//  Học cải thiện
    @PostMapping("/public/question/get-by-section-and-type")
    public ResponseEntity<List<QuestionResponse>> getQuestionsBySectionIdAndType(@RequestBody Map<String, Object> request) {
        Integer sectionId = (Integer) request.get("sectionId");
        String questionType = (String) request.get("questionType");

        // Thực hiện truy vấn dữ liệu dựa trên sectionId và questionType
        List<QuestionResponse> questionList = iQuestionService.getQuestionsBySectionIdAndType(sectionId, questionType).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!questionList.isEmpty()) {
            return new ResponseEntity<>(questionList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(questionList, HttpStatus.NOT_FOUND);
        }
    }

}


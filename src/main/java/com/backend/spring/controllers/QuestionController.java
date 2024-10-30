package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.QuestionMapper;
import com.backend.spring.entities.Question;
import com.backend.spring.payload.request.QuestionRequest;
import com.backend.spring.payload.response.QuestionResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.Question.IQuestionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class QuestionController {

    private final IQuestionService iQuestionService;

    //admin
    @GetMapping("/admin/question/get-all")
    public ResponseEntity<?> getAllQuestions() {
        List<QuestionResponse> questionList = iQuestionService.getAllQuestions().stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Question.GET_DATA_SUCCESS, questionList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/question/get-by-id/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable("id") @Min(1) Integer id) {
        QuestionResponse question = QuestionMapper.mapFromEntityToResponse(iQuestionService.getQuestionById(id));

        if (question != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Question.GET_DATA_SUCCESS, question),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Question.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/question/get-question-by-section/{sectionId}")
    public ResponseEntity<?> getQuestionsBySectionId(@PathVariable("sectionId") @Min(1) Integer sectionId) {
        List<QuestionResponse> questionList = iQuestionService.getQuestionsBySectionId(sectionId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!questionList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Question.GET_DATA_SUCCESS, questionList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Question.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/question/get-question-by-group/{groupId}")
    public ResponseEntity<?> getQuestionsByGroupId(@PathVariable("groupId") @Min(1) Integer groupId) {
        List<QuestionResponse> questionList = iQuestionService.getQuestionsByGroupId(groupId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!questionList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Question.GET_DATA_SUCCESS, questionList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Question.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/question/create")
    public ResponseEntity<?> createQuestion(@ModelAttribute @Valid QuestionRequest questionRequest) {
        try {
            Question createdQuestion = iQuestionService.createQuestion(questionRequest);

            if(createdQuestion != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Question.CREATE_SUCCESS),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Question.CREATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/admin/question/update")
    public ResponseEntity<?> updateQuestion(@ModelAttribute @Valid QuestionRequest questionRequest) {
        try {
            Question updatedQuestion = iQuestionService.updateQuestion(questionRequest);

            if (updatedQuestion != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Question.UPDATE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Question.UPDATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/question/delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable("id") @Min(1) Integer id) {
        try {
            boolean result = iQuestionService.deleteQuestion(id);

            if(result) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.Question.DELETE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.Question.DELETE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/question/update-status/{id}")
    public ResponseEntity<?> updateQuestionStatus(@PathVariable("id") @Min(1) Integer id, @RequestBody Integer newStatus) {
        Question questionUpdate = iQuestionService.updateQuestionStatus(id, newStatus);

        if(questionUpdate != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Question.UPDATE_STATUS_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Question.UPDATE_STATUS_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    //user
    @GetMapping("/public/question/get-question-by-section/{sectionId}/enable")
    public ResponseEntity<?> getQuestionsBySectionIdEnable(@PathVariable("sectionId") @Min(1) Integer sectionId) {
        List<QuestionResponse> questionList = iQuestionService.getQuestionsBySectionId(sectionId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        List<QuestionResponse> questionResponseEnableList = questionList.stream().filter(
                item -> item.getQuestionStatus().equals(EStatus.ENABLE.getValue())
        ).collect(Collectors.toList());

        if (!questionResponseEnableList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Question.GET_DATA_SUCCESS, questionResponseEnableList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Question.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/question/get-question-by-group/{groupId}/enable")
    public ResponseEntity<?> getQuestionsEnableByGroupId(@PathVariable("groupId") @Min(1) Integer groupId) {
        List<QuestionResponse> questionList = iQuestionService.getQuestionsByGroupId(groupId).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        List<QuestionResponse> questionResponseEnableList = questionList.stream().filter(
                item -> item.getQuestionStatus().equals(EStatus.ENABLE.getValue())
        ).collect(Collectors.toList());

        if (!questionResponseEnableList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Question.GET_DATA_SUCCESS, questionResponseEnableList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Question.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

//  Học cải thiện (user)
    @PostMapping("/public/question/get-by-section-and-type")
    public ResponseEntity<?> getQuestionsBySectionIdAndType(@RequestBody Map<String, Object> request) {
        Integer sectionId = (Integer) request.get("sectionId");
        String questionType = (String) request.get("questionType");

        // Thực hiện truy vấn dữ liệu dựa trên sectionId và questionType
        List<QuestionResponse> questionList = iQuestionService.getQuestionsBySectionIdAndType(sectionId, questionType).stream().map(
                QuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!questionList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Question.GET_DATA_SUCCESS, questionList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Question.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

}


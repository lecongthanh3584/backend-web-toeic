package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.VocabularyQuestionMapper;
import com.backend.spring.entities.VocabularyQuestion;
import com.backend.spring.payload.request.VocabularyQuestionRequest;
import com.backend.spring.payload.response.VocabularyQuestionResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.VocabularyQuestion.IVocabularyQuestionService;
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
public class VocabularyQuestionController {

    private final IVocabularyQuestionService iVocabularyQuestionService;

    @GetMapping("/admin/vocabulary-question/get-all")
    public ResponseEntity<?> getAllVocabularyQuestions() {
        List<VocabularyQuestionResponse> vocabularyQuestions = iVocabularyQuestionService.getAllVocabularyQuestions().stream().map(
                VocabularyQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.VocabularyQuestion.GET_DATA_SUCCESS, vocabularyQuestions),
                HttpStatus.OK);
    }

    @GetMapping("/admin/vocabulary-question/get-by-id/{id}")
    public ResponseEntity<?> getVocabularyQuestionById(@PathVariable("id") @Min(1) Integer id) {
        VocabularyQuestionResponse vocabularyQuestion = VocabularyQuestionMapper.mapFromEntityToResponse(
                iVocabularyQuestionService.getVocabularyQuestionById(id)
        );

        if (vocabularyQuestion != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.VocabularyQuestion.GET_DATA_SUCCESS, vocabularyQuestion),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.VocabularyQuestion.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/vocabulary-question/create")
    public ResponseEntity<?> createVocabularyQuestion(@RequestBody @Valid VocabularyQuestionRequest vocabularyQuestionRequest) {
        VocabularyQuestion createdVocabularyQuestion = iVocabularyQuestionService.createVocabularyQuestion(vocabularyQuestionRequest);

        if (createdVocabularyQuestion != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.VocabularyQuestion.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.VocabularyQuestion.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/vocabulary-question/update")
    public ResponseEntity<?> updateVocabularyQuestion(@RequestBody @Valid VocabularyQuestionRequest vocabularyQuestionRequest) {
        VocabularyQuestion updatedVocabularyQuestion = iVocabularyQuestionService.updateVocabularyQuestion(vocabularyQuestionRequest);

        if (updatedVocabularyQuestion != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.VocabularyQuestion.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.VocabularyQuestion.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/vocabulary-question/delete/{id}")
    public ResponseEntity<?> deleteVocabularyQuestion(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iVocabularyQuestionService.deleteVocabularyQuestion(id);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.VocabularyQuestion.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.VocabularyQuestion.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/admin/vocabulary-question/update-status/{id}")
    public ResponseEntity<?> updateVocabularyQuestionStatus(@PathVariable("id") @Min(1) Integer id, @RequestBody Integer newStatus) {
        VocabularyQuestion vocabularyQuestionUpdate = iVocabularyQuestionService.updateVocabularyQuestionStatus(id, newStatus);

        if(vocabularyQuestionUpdate != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.VocabularyQuestion.UPDATE_STATUS_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.VocabularyQuestion.UPDATE_STATUS_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    // Lấy danh sách nội dung ngữ pháp theo topic_id
    @GetMapping("/admin/vocabulary-question/get-by-topic/{topicId}")
    public ResponseEntity<?> getVocabularyQuestionsByTopicId(@PathVariable @Min(1) Integer topicId) {
        List<VocabularyQuestionResponse> vocabularyQuestions = iVocabularyQuestionService.getVocabularyQuestionsByTopicId(topicId).stream().map(
                VocabularyQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!vocabularyQuestions.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.VocabularyQuestion.GET_DATA_SUCCESS, vocabularyQuestions),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.VocabularyQuestion.DATA_NOT_FOUND, vocabularyQuestions),
                    HttpStatus.NOT_FOUND);
        }
    }

    //  Người dùng
    @GetMapping("/public/vocabulary-question/get-by-topic/{topicId}/enable")
    public ResponseEntity<?> getEnableVocabularyQuestionsByTopicId(@PathVariable @Min(1) Integer topicId) {
        List<VocabularyQuestionResponse> vocabularyQuestions = iVocabularyQuestionService.getVocabularyQuestionsByTopicId(topicId).stream().map(
                VocabularyQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        List<VocabularyQuestionResponse> filteredvocabularyQuestions = vocabularyQuestions.stream()
                .filter(vocabularyQuestion -> vocabularyQuestion.getQuestionStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredvocabularyQuestions.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.VocabularyQuestion.GET_DATA_SUCCESS, filteredvocabularyQuestions),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.VocabularyQuestion.DATA_NOT_FOUND, filteredvocabularyQuestions),
                    HttpStatus.NOT_FOUND);
        }
    }

}

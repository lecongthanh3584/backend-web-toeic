package com.backend.spring.controller;

import com.backend.spring.enums.EStatus;
import com.backend.spring.mapper.VocabularyQuestionMapper;
import com.backend.spring.entity.VocabularyQuestion;
import com.backend.spring.payload.request.VocabularyQuestionRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.VocabularyQuestionResponse;
import com.backend.spring.service.VocabularyQuestion.IVocabularyQuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class VocabularyQuestionController {

    @Autowired
    private IVocabularyQuestionService iVocabularyQuestionService;

    @GetMapping("/admin/vocabulary-question/get-all")
    public ResponseEntity<List<VocabularyQuestionResponse>> getAllVocabularyQuestions() {
        List<VocabularyQuestionResponse> vocabularyQuestions = iVocabularyQuestionService.getAllVocabularyQuestions().stream().map(
                VocabularyQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(vocabularyQuestions, HttpStatus.OK);
    }

    @GetMapping("/admin/vocabulary-question/get-by-id/{id}")
    public ResponseEntity<VocabularyQuestionResponse> getVocabularyQuestionById(@PathVariable Integer id) {
        VocabularyQuestionResponse vocabularyQuestion = VocabularyQuestionMapper.mapFromEntityToResponse(
                iVocabularyQuestionService.getVocabularyQuestionById(id));

        if (vocabularyQuestion != null) {
            return new ResponseEntity<>(vocabularyQuestion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/vocabulary-question/create")
    public ResponseEntity<MessageResponse> createVocabularyQuestion(@RequestBody @Valid VocabularyQuestionRequest vocabularyQuestionRequest) {
        VocabularyQuestion createdVocabularyQuestion = iVocabularyQuestionService.createVocabularyQuestion(vocabularyQuestionRequest);

        if (createdVocabularyQuestion != null) {
            return ResponseEntity.ok(new MessageResponse("Thêm câu hỏi từ vựng theo chủ đề thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Thêm câu hỏi từ vựng theo chủ đề thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/vocabulary-question/update")
    public ResponseEntity<MessageResponse> updateVocabularyQuestion(@RequestBody @Valid VocabularyQuestionRequest vocabularyQuestionRequest) {
        VocabularyQuestion updatedVocabularyQuestion = iVocabularyQuestionService.updateVocabularyQuestion(vocabularyQuestionRequest);

        if (updatedVocabularyQuestion != null) {
            return ResponseEntity.ok(new MessageResponse("Cập nhật câu hỏi từ vựng theo chủ đề thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật câu hỏi từ vựng theo chủ đề thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/vocabulary-question/delete/{id}")
    public ResponseEntity<MessageResponse> deleteVocabularyQuestion(@PathVariable Integer id) {
        boolean result = iVocabularyQuestionService.deleteVocabularyQuestion(id);

        if(result) {
            return ResponseEntity.ok(new MessageResponse("Xóa câu hỏi từ vựng theo chủ đề thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Xóa câu hỏi từ vựng theo chủ đề thất bại!"), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/admin/vocabulary-question/update-status/{id}")
    public ResponseEntity<MessageResponse> updateVocabularyQuestionStatus(@PathVariable Integer id, @RequestBody Integer newStatus) {
        VocabularyQuestion vocabularyQuestionUpdate = iVocabularyQuestionService.updateVocabularyQuestionStatus(id, newStatus);

        if(vocabularyQuestionUpdate != null) {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái câu hỏi thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái câu hỏi thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    // Lấy danh sách nội dung ngữ pháp theo topic_id
    @GetMapping("/admin/vocabulary-question/get-by-topic/{topicId}")
    public ResponseEntity<List<VocabularyQuestionResponse>> getVocabularyQuestionsByTopicId(@PathVariable Integer topicId) {
        List<VocabularyQuestionResponse> vocabularyQuestions = iVocabularyQuestionService.getVocabularyQuestionsByTopicId(topicId).stream().map(
                VocabularyQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!vocabularyQuestions.isEmpty()) {
            return new ResponseEntity<>(vocabularyQuestions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(vocabularyQuestions, HttpStatus.NOT_FOUND);
        }
    }

    //  Người dùng
    @GetMapping("/public/vocabulary-question/get-by-topic/{topicId}/enable")
    public ResponseEntity<List<VocabularyQuestionResponse>> getEnableVocabularyQuestionsByTopicId(@PathVariable Integer topicId) {
        List<VocabularyQuestionResponse> vocabularyQuestions = iVocabularyQuestionService.getVocabularyQuestionsByTopicId(topicId).stream().map(
                VocabularyQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        List<VocabularyQuestionResponse> filteredvocabularyQuestions = vocabularyQuestions.stream()
                .filter(vocabularyQuestion -> vocabularyQuestion.getQuestionStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredvocabularyQuestions.isEmpty()) {
            return new ResponseEntity<>(filteredvocabularyQuestions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(filteredvocabularyQuestions, HttpStatus.NOT_FOUND);
        }
    }

}

package com.backend.spring.controller;

import com.backend.spring.enums.EStatus;
import com.backend.spring.mapper.GrammarQuestionMapper;
import com.backend.spring.entity.GrammarQuestion;
import com.backend.spring.payload.request.GrammarQuestionRequest;
import com.backend.spring.payload.response.GrammarQuestionResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.service.GrammarQuestion.IGrammarQuestionService;
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
public class GrammarQuestionController {

    @Autowired
    private IGrammarQuestionService iGrammarQuestionService;

    @GetMapping("/admin/grammar-question/get-all")
    public ResponseEntity<List<GrammarQuestionResponse>> getAllGrammarQuestions() {
        List<GrammarQuestionResponse> grammarQuestions = iGrammarQuestionService.getAllGrammarQuestions().stream().map(
                GrammarQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(grammarQuestions, HttpStatus.OK);
    }

    @GetMapping("/admin/grammar-question/get-by-id/{id}")
    public ResponseEntity<GrammarQuestionResponse> getGrammarQuestionById(@PathVariable Integer id) {
        GrammarQuestionResponse grammarQuestion = GrammarQuestionMapper.mapFromEntityToResponse(iGrammarQuestionService.getGrammarQuestionById(id));

        if (grammarQuestion != null) {
            return new ResponseEntity<>(grammarQuestion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/grammar-question/create")
    public ResponseEntity<MessageResponse> createGrammarQuestion(@RequestBody @Valid GrammarQuestionRequest grammarQuestionRequest) {
        GrammarQuestion createdGrammarQuestion = iGrammarQuestionService.createGrammarQuestion(grammarQuestionRequest);

        if (createdGrammarQuestion != null) {
            return ResponseEntity.ok(new MessageResponse("Thêm câu hỏi ngữ pháp thành công!"));
        } else {
           return new ResponseEntity<>(new MessageResponse("Thêm câu hỏi ngữ pháp thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/grammar-question/update")
    public ResponseEntity<MessageResponse> updateGrammarQuestion(@RequestBody @Valid GrammarQuestionRequest grammarQuestionRequest) {
        GrammarQuestion updatedGrammarQuestion = iGrammarQuestionService.updateGrammarQuestion(grammarQuestionRequest);

        if (updatedGrammarQuestion != null) {
            return ResponseEntity.ok(new MessageResponse("Cập nhật câu hỏi ngữ pháp thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật câu hỏi ngữ pháp thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/grammar-question/delete/{id}")
    public ResponseEntity<MessageResponse> deleteGrammarQuestion(@PathVariable Integer id) {
        boolean result = iGrammarQuestionService.deleteGrammarQuestion(id);

        if(result) {
            return ResponseEntity.ok(new MessageResponse("Xóa câu hỏi ngữ pháp thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Xóa câu hỏi ngữ pháp thất bại"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/grammar-question/update-status/{id}")
    public ResponseEntity<MessageResponse> updateGrammarQuestionStatus(@PathVariable Integer id, @RequestBody Integer newStatus) {
        GrammarQuestion grammarQuestion = iGrammarQuestionService.updateGrammarQuestionStatus(id, newStatus);

        if(grammarQuestion != null) {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái câu hỏi thành công"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái câu hỏi thất bại"), HttpStatus.BAD_REQUEST);
        }
    }

    // Lấy danh sách nội dung câu hỏi theo grammar_id
    @GetMapping("/admin/grammar-question/get-question-by-grammar/{grammarId}")
    public ResponseEntity<List<GrammarQuestionResponse>> getGrammarQuestionsByGrammarId(@PathVariable Integer grammarId) {
        List<GrammarQuestionResponse> grammarQuestions = iGrammarQuestionService.getGrammarQuestionsByGrammarId(grammarId).stream().map(
                GrammarQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!grammarQuestions.isEmpty()) {
            return new ResponseEntity<>(grammarQuestions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(grammarQuestions, HttpStatus.NOT_FOUND);
        }
    }

    //  Người dùng
    @GetMapping("/public/grammar-question/get-question-by-grammar/{grammarId}/enable")
    public ResponseEntity<List<GrammarQuestionResponse>> getEnableGrammarQuestionsByGrammarId(@PathVariable Integer grammarId) {
        List<GrammarQuestionResponse> grammarQuestions = iGrammarQuestionService.getGrammarQuestionsByGrammarId(grammarId).stream().map(
                GrammarQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các GrammarContent có grammarContentStatus là 1 (enable)
        List<GrammarQuestionResponse> filteredGrammarQuestions = grammarQuestions.stream()
                .filter(item -> item.getQuestionStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredGrammarQuestions.isEmpty()) {
            return new ResponseEntity<>(filteredGrammarQuestions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(filteredGrammarQuestions, HttpStatus.NOT_FOUND);
        }
    }


}

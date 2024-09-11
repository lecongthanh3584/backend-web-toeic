package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.GrammarQuestionMapper;
import com.backend.spring.entity.GrammarQuestion;
import com.backend.spring.payload.request.GrammarQuestionRequest;
import com.backend.spring.payload.response.GrammarQuestionResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.GrammarQuestion.IGrammarQuestionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Validated
public class GrammarQuestionController {

    @Autowired
    private IGrammarQuestionService iGrammarQuestionService;

    @GetMapping("/admin/grammar-question/get-all")
    public ResponseEntity<?> getAllGrammarQuestions() {
        List<GrammarQuestionResponse> grammarQuestions = iGrammarQuestionService.getAllGrammarQuestions().stream().map(
                GrammarQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.GrammarQuestion.GET_DATA_SUCCESS, grammarQuestions),
                HttpStatus.OK);
    }

    @GetMapping("/admin/grammar-question/get-by-id/{id}")
    public ResponseEntity<?> getGrammarQuestionById(@PathVariable("id") @Min(1) Integer id) {
        GrammarQuestionResponse grammarQuestion = GrammarQuestionMapper.mapFromEntityToResponse(iGrammarQuestionService.getGrammarQuestionById(id));

        if (grammarQuestion != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.GrammarQuestion.GET_DATA_SUCCESS, grammarQuestion),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.GrammarQuestion.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/grammar-question/create")
    public ResponseEntity<?> createGrammarQuestion(@RequestBody @Valid GrammarQuestionRequest grammarQuestionRequest) {
        GrammarQuestion createdGrammarQuestion = iGrammarQuestionService.createGrammarQuestion(grammarQuestionRequest);

        if (createdGrammarQuestion != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.GrammarQuestion.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.GrammarQuestion.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/grammar-question/update")
    public ResponseEntity<?> updateGrammarQuestion(@RequestBody @Valid GrammarQuestionRequest grammarQuestionRequest) {
        GrammarQuestion updatedGrammarQuestion = iGrammarQuestionService.updateGrammarQuestion(grammarQuestionRequest);

        if (updatedGrammarQuestion != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.GrammarQuestion.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.GrammarQuestion.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/grammar-question/delete/{id}")
    public ResponseEntity<?> deleteGrammarQuestion(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iGrammarQuestionService.deleteGrammarQuestion(id);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.GrammarQuestion.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.GrammarQuestion.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/grammar-question/update-status/{id}")
    public ResponseEntity<?> updateGrammarQuestionStatus(@PathVariable("id") @Min(1) Integer id, @RequestBody Integer newStatus) {
        GrammarQuestion grammarQuestion = iGrammarQuestionService.updateGrammarQuestionStatus(id, newStatus);

        if(grammarQuestion != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.GrammarQuestion.UPDATE_STATUS_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.GrammarQuestion.UPDATE_STATUS_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    // Lấy danh sách nội dung câu hỏi theo grammar_id
    @GetMapping("/admin/grammar-question/get-question-by-grammar/{grammarId}")
    public ResponseEntity<?> getGrammarQuestionsByGrammarId(@PathVariable("grammarId") @Min(1) Integer grammarId) {
        List<GrammarQuestionResponse> grammarQuestions = iGrammarQuestionService.getGrammarQuestionsByGrammarId(grammarId).stream().map(
                GrammarQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!grammarQuestions.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.GrammarQuestion.GET_DATA_SUCCESS, grammarQuestions),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.GrammarQuestion.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    //  Người dùng
    @GetMapping("/public/grammar-question/get-question-by-grammar/{grammarId}/enable")
    public ResponseEntity<?> getEnableGrammarQuestionsByGrammarId(@PathVariable("grammarId") @Min(1) Integer grammarId) {
        List<GrammarQuestionResponse> grammarQuestions = iGrammarQuestionService.getGrammarQuestionsByGrammarId(grammarId).stream().map(
                GrammarQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các GrammarContent có grammarContentStatus là 1 (enable)
        List<GrammarQuestionResponse> filteredGrammarQuestions = grammarQuestions.stream()
                .filter(item -> item.getQuestionStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredGrammarQuestions.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.GrammarQuestion.GET_DATA_SUCCESS, filteredGrammarQuestions),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.GrammarQuestion.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }


}

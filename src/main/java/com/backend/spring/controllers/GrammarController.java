package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.ExamMapper;
import com.backend.spring.mapper.GrammarMapper;
import com.backend.spring.entities.Grammar;
import com.backend.spring.payload.request.GrammarRequest;
import com.backend.spring.payload.response.ExamResponse;
import com.backend.spring.payload.response.GrammarResponse;
import com.backend.spring.payload.response.main.PaginationData;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.Grammar.IGrammarService;
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
public class GrammarController {

    private final IGrammarService iGrammarService;

//  Admin
    @GetMapping("/admin/grammar/get-all")
    public ResponseEntity<?> getAllGrammar(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "created_at:desc") String... sortBys
    ) {
        Page<Grammar> grammarPage = iGrammarService.getAllGrammar(page, keyword, sortBys);

        PaginationData paginationData = PaginationData.builder().totalPage(grammarPage.getTotalPages()).totalElement(grammarPage.getTotalElements())
                .pageNumber(grammarPage.getPageable().getPageNumber()).pageSize(grammarPage.getPageable().getPageSize()).build();

        List<GrammarResponse> grammarResponseList = grammarPage.getContent().stream().map(
                GrammarMapper::mapFromEntityToResponse
        ).toList();

        if (!grammarResponseList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Grammar.GET_DATA_SUCCESS,
                    paginationData, grammarResponseList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Grammar.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/grammar/get-by-id/{id}")
    public ResponseEntity<?> getGrammarById(@PathVariable("id") @Min(1) Integer id) {
        GrammarResponse grammarResponse = GrammarMapper.mapFromEntityToResponse(iGrammarService.getGrammarById(id));

        if (grammarResponse != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Grammar.GET_DATA_SUCCESS, grammarResponse),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Grammar.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/grammar/get-name-by-id/{id}")
    public ResponseEntity<?> getGrammarNameById(@PathVariable("id") @Min(1) Integer id) {
        String grammarName = iGrammarService.getGrammarNameById(id);

        if (grammarName != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Grammar.GET_DATA_SUCCESS, grammarName),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Grammar.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/grammar/create")
    public ResponseEntity<?> createGrammar(@RequestBody @Valid GrammarRequest grammarRequest) {

        // Kiểm tra xem tên grammar đã tồn tại chưa
        if (iGrammarService.isGrammarNameExists(grammarRequest.getGrammarName())) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_FIELD_EXIST.getValue(), MessageConstant.Grammar.GRAMMAR_NAME_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }

        Grammar createdGrammar = iGrammarService.createGrammar(grammarRequest);

        if(createdGrammar != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Grammar.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Grammar.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/grammar/update")
    public ResponseEntity<?> updateGrammar(@RequestBody @Valid GrammarRequest grammarRequest) {
        // Kiểm tra trùng lặp tên grammar (nếu tên đã thay đổi)
        if (iGrammarService.isGrammarNameExists(grammarRequest.getGrammarName())) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_FIELD_EXIST.getValue(), MessageConstant.Grammar.GRAMMAR_NAME_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }

        Grammar updatedGrammar = iGrammarService.updateGrammar(grammarRequest);

        if (updatedGrammar != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Grammar.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Grammar.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/grammar/delete/{id}")
    public ResponseEntity<?> deleteGrammar(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iGrammarService.deleteGrammar(id);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.Grammar.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.Grammar.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/grammar/update-status/{id}")
    public ResponseEntity<?> updateGrammarStatus(@PathVariable("id") @Min(1) Integer grammarId, @RequestBody Integer newStatus) {
        Grammar grammar = iGrammarService.updateGrammarStatus(grammarId, newStatus);

        if(grammar != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Grammar.UPDATE_STATUS_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Grammar.UPDATE_STATUS_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

//  Người dùng
    @GetMapping("/public/grammar/get-all/enable")
    public ResponseEntity<?> getAllEnableGrammar(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        Page<Grammar> grammarPage = iGrammarService.getAllGrammarEnable(page, keyword);

        PaginationData paginationData = PaginationData.builder().totalPage(grammarPage.getTotalPages()).totalElement(grammarPage.getTotalElements())
                .pageNumber(grammarPage.getPageable().getPageNumber()).pageSize(grammarPage.getPageable().getPageSize()).build();

        List<GrammarResponse> grammarResponseList = grammarPage.getContent().stream().map(
                GrammarMapper::mapFromEntityToResponse
        ).toList();

        if (!grammarResponseList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Grammar.GET_DATA_SUCCESS,
                    paginationData, grammarResponseList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Grammar.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

}

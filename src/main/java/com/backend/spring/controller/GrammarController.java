package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.mapper.GrammarMapper;
import com.backend.spring.entity.Grammar;
import com.backend.spring.payload.request.GrammarRequest;
import com.backend.spring.payload.response.GrammarResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.Grammar.IGrammarService;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class GrammarController {

    @Autowired
    private IGrammarService iGrammarService;

//  Admin
    @GetMapping("/admin/grammar/get-all")
    public ResponseData<List<GrammarResponse>> getAllGrammar() {
        List<GrammarResponse> grammarList = iGrammarService.getAllGrammar().stream().map(
                GrammarMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.Grammar.GET_DATA_SUCCESS, grammarList);
    }

//  Người dùng
    @GetMapping("/public/grammar/get-all/enable")
    public ResponseData<List<GrammarResponse>> getAllEnableGrammar() {
        List<GrammarResponse> grammarList = iGrammarService.getAllGrammar().stream().map(
                GrammarMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các Grammar có grammarStatus là 1
        List<GrammarResponse> filteredGrammarList = grammarList.stream()
                .filter(grammar -> grammar.getGrammarStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredGrammarList.isEmpty()) {
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.Grammar.GET_DATA_SUCCESS, filteredGrammarList);
        } else {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), MessageConstant.Grammar.DATA_NOT_FOUND);
        }
    }

    @GetMapping("/admin/grammar/get-by-id/{id}")
    public ResponseData<GrammarResponse> getGrammarById(@PathVariable("id") @Min(1) Integer id) {
        GrammarResponse grammarResponse = GrammarMapper.mapFromEntityToResponse(iGrammarService.getGrammarById(id));

        if (grammarResponse != null) {
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.Grammar.GET_DATA_SUCCESS, grammarResponse);
        } else {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), MessageConstant.Grammar.DATA_NOT_FOUND);
        }
    }

    @GetMapping("/admin/grammar/get-name-by-id/{id}")
    public ResponseData<?> getGrammarNameById(@PathVariable("id") @Min(1) Integer id) {
        String grammarName = iGrammarService.getGrammarNameById(id);

        if (grammarName != null) {
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.Grammar.GET_DATA_SUCCESS, grammarName);
        } else {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), MessageConstant.Grammar.DATA_NOT_FOUND);
        }
    }

    @PostMapping("/admin/grammar/create")
    public ResponseData<MessageResponse> createGrammar(@RequestBody @Valid GrammarRequest grammarRequest) {

        // Kiểm tra xem tên grammar đã tồn tại chưa
        if (iGrammarService.isGrammarNameExists(grammarRequest.getGrammarName())) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.Grammar.GRAMMAR_NAME_EXISTED);
        }

        Grammar createdGrammar = iGrammarService.createGrammar(grammarRequest);

        if(createdGrammar != null) {
            return new ResponseData<>(HttpStatus.CREATED.value(), MessageConstant.Grammar.CREATE_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.Grammar.CREATE_FAILED);
        }
    }

    @PutMapping("/admin/grammar/update")
    public ResponseData<MessageResponse> updateGrammar(@RequestBody @Valid GrammarRequest grammarRequest) {
        // Kiểm tra trùng lặp tên grammar (nếu tên đã thay đổi)
        if (iGrammarService.isGrammarNameExists(grammarRequest.getGrammarName())) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.Grammar.GRAMMAR_NAME_EXISTED);
        }

        Grammar updatedGrammar = iGrammarService.updateGrammar(grammarRequest);

        if (updatedGrammar != null) {
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.Grammar.UPDATE_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.Grammar.UPDATE_FAILED);
        }
    }

    @DeleteMapping("/admin/grammar/delete/{id}")
    public ResponseData<MessageResponse> deleteGrammar(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iGrammarService.deleteGrammar(id);

        if(result) {
             return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.Grammar.DELETE_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.Grammar.DELETE_FAILED);
        }
    }

    @PutMapping("/admin/grammar/update-status/{id}")
    public ResponseData<MessageResponse> updateGrammarStatus(@PathVariable("id") @Min(1) Integer grammarId, @RequestBody Integer newStatus) {
       Grammar grammar = iGrammarService.updateGrammarStatus(grammarId, newStatus);

       if(grammar != null) {
           return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.Grammar.UPDATE_STATUS_SUCCESS);
       } else {
           return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.Grammar.UPDATE_STATUS_FAILED);
       }
    }

}

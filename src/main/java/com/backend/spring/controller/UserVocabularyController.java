package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.UserVocabularyMapper;
import com.backend.spring.entity.UserVocabulary;
import com.backend.spring.payload.request.UserVocabularyRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.UserVocabularyResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.UserVocabulary.IUserVocabularyService;
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
public class UserVocabularyController {

    @Autowired
    private IUserVocabularyService iUserVocabularyService;

    @PostMapping("/user/user-vocabulary/create")
    public ResponseEntity<?> createUserVocabulary(@RequestBody @Valid UserVocabularyRequest userVocabularyRequest) {
        UserVocabulary createdUserVocabulary = iUserVocabularyService.createUserVocabulary(userVocabularyRequest);

        if(createdUserVocabulary != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.UserVocabulary.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.UserVocabulary.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/user-vocabulary/get-user-vocabularies-by-user/{userId}")
    public ResponseEntity<?> getUserVocabulariesByUserId(@PathVariable("userId") @Min(1) Integer userId) {
        List<UserVocabularyResponse> userVocabularies = iUserVocabularyService.getUserVocabulariesByUserId(userId)
                .stream().map(UserVocabularyMapper::mapFromEntityToResponse).collect(Collectors.toList());

        if(!userVocabularies.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.UserVocabulary.GET_DATA_SUCCESS, userVocabularies),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.UserVocabulary.DATA_NOT_FOUND, userVocabularies),
                    HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/user-vocabulary/delete/{userVocabularyId}")
    public ResponseEntity<?> deleteUserVocabulary(@PathVariable @Min(1) Integer userVocabularyId) {
        boolean result = iUserVocabularyService.deleteUserVocabulary(userVocabularyId);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.UserVocabulary.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.UserVocabulary.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }
}


package com.backend.spring.controller;

import com.backend.spring.mapper.UserVocabularyMapper;
import com.backend.spring.entity.UserVocabulary;
import com.backend.spring.payload.request.UserVocabularyRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.UserVocabularyResponse;
import com.backend.spring.service.UserVocabulary.IUserVocabularyService;
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
public class UserVocabularyController {

    @Autowired
    private IUserVocabularyService iUserVocabularyService;

    @PostMapping("/user/user-vocabulary/create")
    public ResponseEntity<MessageResponse> createUserVocabulary(@RequestBody @Valid UserVocabularyRequest userVocabularyRequest) {
        UserVocabulary createdUserVocabulary = iUserVocabularyService.createUserVocabulary(userVocabularyRequest);

        if(createdUserVocabulary != null) {
            return ResponseEntity.ok(new MessageResponse("Thêm từ vựng cá nhân thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Thêm từ vựng cá nhân thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/user-vocabulary/get-user-vocabularies-by-user/{userId}")
    public ResponseEntity<List<UserVocabularyResponse>> getUserVocabulariesByUserId(@PathVariable Integer userId) {
        List<UserVocabularyResponse> userVocabularies = iUserVocabularyService.getUserVocabulariesByUserId(userId)
                .stream().map(UserVocabularyMapper::mapFromEntityToResponse).collect(Collectors.toList());

        if(!userVocabularies.isEmpty()) {
            return ResponseEntity.ok(userVocabularies);
        } else {
            return new ResponseEntity<>(userVocabularies, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/user-vocabulary/delete/{userVocabularyId}")
    public ResponseEntity<?> deleteUserVocabulary(@PathVariable Integer userVocabularyId) {
        boolean result = iUserVocabularyService.deleteUserVocabulary(userVocabularyId);

        if(result) {
            return new ResponseEntity<>(new MessageResponse("Xoá từ vựng thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Xoá tự vựng thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }
}


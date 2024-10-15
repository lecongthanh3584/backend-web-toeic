package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.QuestionGroupMapper;
import com.backend.spring.entities.QuestionGroup;
import com.backend.spring.payload.request.QuestionGroupRequest;
import com.backend.spring.payload.response.QuestionGroupResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.QuestionGroup.IQuestionGroupService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class QuestionGroupController {

    private final IQuestionGroupService iQuestionGroupService;

    @GetMapping("/admin/question-group/get-all")
    public ResponseEntity<?> getAllQuestionGroups() {
        List<QuestionGroupResponse> questionGroupList = iQuestionGroupService.getAllQuestionGroups().stream().map(
                QuestionGroupMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.QuestionGroup.GET_DATA_SUCCESS, questionGroupList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/question-group/get-by-id/{id}")
    public ResponseEntity<?> getQuestionGroupById(@PathVariable("id") @Min(1) Integer id) {
        QuestionGroupResponse questionGroup = QuestionGroupMapper.mapFromEntityToResponse(iQuestionGroupService.getQuestionGroupById(id));

        if (questionGroup != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.QuestionGroup.GET_DATA_SUCCESS, questionGroup),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.QuestionGroup.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/question-group/create")
    public ResponseEntity<?> createQuestionGroup(@ModelAttribute QuestionGroupRequest questionGroupRequest) {
        try {
            QuestionGroup createdGroup = iQuestionGroupService.createQuestionGroup(questionGroupRequest);

            if(createdGroup != null) {
                Integer createdGroupId = createdGroup.getGroupId(); // Lấy ID của đối tượng vừa tạo

                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.QuestionGroup.CREATE_SUCCESS, createdGroupId),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.QuestionGroup.CREATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/question-group/update")
    public ResponseEntity<?> updateQuestionGroup(@ModelAttribute QuestionGroupRequest questionGroupRequest) {
        try {
            QuestionGroup updatedGroup = iQuestionGroupService.updateQuestionGroup(questionGroupRequest);

            if (updatedGroup != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.QuestionGroup.UPDATE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.QuestionGroup.UPDATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/question-group/delete/{id}")
    public ResponseEntity<?> deleteQuestionGroup(@PathVariable("id") @Min(1) Integer id) {
        try {
            boolean result = iQuestionGroupService.deleteQuestionGroup(id);

            if(result) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.QuestionGroup.DELETE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.QuestionGroup.DELETE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

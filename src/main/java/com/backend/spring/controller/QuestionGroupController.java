package com.backend.spring.controller;

import com.backend.spring.mapper.QuestionGroupMapper;
import com.backend.spring.entity.QuestionGroup;
import com.backend.spring.payload.request.QuestionGroupRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.QuestionGroupResponse;
import com.backend.spring.service.QuestionGroup.IQuestionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class QuestionGroupController {

    @Autowired
    private IQuestionGroupService iQuestionGroupService;

    @GetMapping("/admin/question-group/get-all")
    public ResponseEntity<List<QuestionGroupResponse>> getAllQuestionGroups() {
        List<QuestionGroupResponse> questionGroupList = iQuestionGroupService.getAllQuestionGroups().stream().map(
                QuestionGroupMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(questionGroupList, HttpStatus.OK);
    }

    @GetMapping("/admin/question-group/get-by-id/{id}")
    public ResponseEntity<QuestionGroupResponse> getQuestionGroupById(@PathVariable Integer id) {
        QuestionGroupResponse questionGroup = QuestionGroupMapper.mapFromEntityToResponse(iQuestionGroupService.getQuestionGroupById(id));

        if (questionGroup != null) {
            return new ResponseEntity<>(questionGroup, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/question-group/create")
    public ResponseEntity<?> createQuestionGroup(@ModelAttribute QuestionGroupRequest questionGroupRequest) {
        try {
            QuestionGroup createdGroup = iQuestionGroupService.createQuestionGroup(questionGroupRequest);

            if(createdGroup != null) {
                Integer createdGroupId = createdGroup.getGroupId(); // Lấy ID của đối tượng vừa tạo

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("groupId", createdGroupId);
                response.put("message", "Thêm nhóm câu hỏi thành công!");

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Thêm nhóm câu hỏi thất bại!");

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi ở: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/question-group/update")
    public ResponseEntity<MessageResponse> updateQuestionGroup(@ModelAttribute QuestionGroupRequest questionGroupRequest) {
        try {
            QuestionGroup updatedGroup = iQuestionGroupService.updateQuestionGroup(questionGroupRequest);

            if (updatedGroup != null) {
                return ResponseEntity.ok(new MessageResponse("Cập nhật nhóm câu hỏi thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Cập nhật nhóm câu hỏi thất bại!"), HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi khi cập nhật nhóm câu hỏi: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/question-group/delete/{id}")
    public ResponseEntity<MessageResponse> deleteQuestionGroup(@PathVariable Integer id) {
        try {
            boolean result = iQuestionGroupService.deleteQuestionGroup(id);

            if(result) {
                return ResponseEntity.ok(new MessageResponse("Xóa nhóm câu hỏi thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Xoá nhóm câu hỏi thất bại!"), HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Có lỗi ở: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

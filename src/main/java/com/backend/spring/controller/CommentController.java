package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.CommentMapper;
import com.backend.spring.payload.request.CommentRequest;
import com.backend.spring.payload.response.CommentResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.Comment.ICommentService;
import jakarta.validation.Valid;
import org.apache.xmlbeans.impl.jam.mutable.MElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Validated
public class CommentController {

    @Autowired
    private ICommentService iCommentService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/public/comment/get-all-root-comment")
    public ResponseEntity<?> getAllRootComments() {
        List<CommentResponse> commentResponseList = iCommentService.getAllRootComments().stream().map(
                CommentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Comment.GET_DATA_SUCCESS, commentResponseList),
                HttpStatus.OK);
    }

    @GetMapping("/user/comment/get-root-comment-by-user/{userId}")
    public ResponseEntity<?> getRootCommentsByUserId(@PathVariable Integer userId) {
        List<CommentResponse> rootCommentResponse = iCommentService.getRootCommentsByUserId(userId).stream().map(
                CommentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Comment.GET_DATA_SUCCESS, rootCommentResponse),
                HttpStatus.OK);
    }

    @PostMapping("/user/comment/create")
    public ResponseEntity<?> createComment(@RequestBody @Valid CommentRequest commentRequest) {
        CommentResponse createdComment = CommentMapper.mapFromEntityToResponse(iCommentService.createComment(commentRequest));

        if (createdComment != null) {
            if (commentRequest.getParentId() != null) {
                // Gửi thông báo chỉ khi có trả lời cho bình luận cha
                messagingTemplate.convertAndSend("/topic/newComment", createdComment);
            }
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Comment.CREATE_SUCCESS), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Comment.CREATE_FAILED), HttpStatus.BAD_REQUEST);
        }
    }
}

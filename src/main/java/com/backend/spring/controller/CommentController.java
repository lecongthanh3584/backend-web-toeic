package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.mapper.CommentMapper;
import com.backend.spring.payload.request.CommentRequest;
import com.backend.spring.payload.response.CommentResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.Comment.ICommentService;
import jakarta.validation.Valid;
import org.apache.xmlbeans.impl.jam.mutable.MElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class CommentController {

    @Autowired
    private ICommentService iCommentService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/public/comment/get-all-root-comment")
    public ResponseData<List<CommentResponse>> getAllRootComments() {
        List<CommentResponse> commentResponseList = iCommentService.getAllRootComments().stream().map(
                CommentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.Comment.GET_DATA_SUCCESS, commentResponseList);
    }

    @GetMapping("/user/comment/get-root-comment-by-user/{userId}")
    public ResponseData<List<CommentResponse>> getRootCommentsByUserId(@PathVariable Integer userId) {
        List<CommentResponse> rootCommentResponse = iCommentService.getRootCommentsByUserId(userId).stream().map(
                CommentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.Comment.GET_DATA_SUCCESS, rootCommentResponse);
    }

    @PostMapping("/user/comment/create")
    public ResponseData<?> createComment(@RequestBody @Valid CommentRequest commentRequest) {
        CommentResponse createdComment = CommentMapper.mapFromEntityToResponse(iCommentService.createComment(commentRequest));

        if (createdComment != null) {
            if (commentRequest.getParentId() != null) {
                // Gửi thông báo chỉ khi có trả lời cho bình luận cha
                messagingTemplate.convertAndSend("/topic/newComment", createdComment);
            }
            return new ResponseData<>(HttpStatus.CREATED.value(), MessageConstant.Comment.CREATE_SUCCESS, createdComment);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.Comment.CREATE_FAILED);
        }
    }
}

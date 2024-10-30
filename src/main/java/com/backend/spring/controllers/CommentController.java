package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.CommentMapper;
import com.backend.spring.payload.request.CommentRequest;
import com.backend.spring.payload.response.CommentResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.Comment.ICommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService iCommentService;

    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/public/comment/get-all-root-comment")
    public ResponseEntity<?> getAllRootComments() {
        List<CommentResponse> commentResponseList = iCommentService.getAllRootComments().stream().map(
                CommentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Comment.GET_DATA_SUCCESS, commentResponseList),
                HttpStatus.OK);
    }

    //user
    @GetMapping("/user/comment/get-root-comment-by-user/{userId}")
    public ResponseEntity<?> getRootCommentsByUserId(@PathVariable Integer userId) {
        List<CommentResponse> rootCommentResponse = iCommentService.getRootCommentsByUserId(userId).stream().map(
                CommentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Comment.GET_DATA_SUCCESS, rootCommentResponse),
                HttpStatus.OK);
    }

    @MessageMapping("/user/comment/create")
    @SendTo(MessageConstant.WebSocketComment.ADD_NEW_COMMENT)
    public ResponseEntity<?> createComment(@Payload @Valid CommentRequest commentRequest) {
        CommentResponse createdComment = CommentMapper.mapFromEntityToResponse(iCommentService.createComment(commentRequest));

        if (createdComment != null) {
            if (commentRequest.getParentId() != null) {
                // Gửi thông báo chỉ khi có trả lời cho bình luận cha
                messagingTemplate.convertAndSend(MessageConstant.WebSocketComment.ADD_NEW_COMMENT, createdComment);
            }
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Comment.CREATE_SUCCESS), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Comment.CREATE_FAILED), HttpStatus.BAD_REQUEST);
        }
    }
}

package com.backend.spring.service.Comment;

import com.backend.spring.entity.Comment;
import com.backend.spring.payload.request.CommentRequest;

import java.util.List;

public interface ICommentService {
    List<Comment> getAllRootComments();
    List<Comment> getRootCommentsByUserId(Integer userId);
    Comment createComment(CommentRequest commentRequest);
}

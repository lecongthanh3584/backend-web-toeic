package com.backend.spring.services.Comment;

import com.backend.spring.entities.Comment;
import com.backend.spring.payload.request.CommentRequest;

import java.util.List;

public interface ICommentService {
    List<Comment> getAllRootComments();
    List<Comment> getRootCommentsByUserId(Integer userId);
    Comment createComment(CommentRequest commentRequest);
}

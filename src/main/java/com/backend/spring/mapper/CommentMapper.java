package com.backend.spring.mapper;

import com.backend.spring.entities.Comment;
import com.backend.spring.payload.response.CommentResponse;

public class CommentMapper {
    public static CommentResponse mapFromEntityToResponse(Comment comment) {
        if(comment == null) {
            return null;
        }

        return new CommentResponse(
                comment.getCommentId(),
                comment.getText(),
                comment.getUser(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getParentComment(),
                comment.getReplies()
        );
    }
}

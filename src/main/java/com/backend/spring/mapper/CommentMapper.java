package com.backend.spring.mapper;

import com.backend.spring.entity.Comment;
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
                comment.getDate(),
                comment.getParentComment(),
                comment.getReplies()
        );
    }
}

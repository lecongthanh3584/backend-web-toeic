package com.backend.spring.payload.response;

import com.backend.spring.entity.Comment;
import com.backend.spring.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CommentResponse {
    private Integer commentId;
    private String text;
    private User user;
    private LocalDateTime date;
    private Comment parentcomment;
    private List<Comment> replies;

}

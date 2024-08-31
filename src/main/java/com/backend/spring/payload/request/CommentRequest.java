package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    private Integer commentId;

    @NotNull
    private Integer userId;

    @NotBlank(message = "Nội dung bình luận không được để trống!")
    private String text;

    private Integer parentId;

}
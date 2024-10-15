package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NoteRequest {
    private Integer noteId;

    @NotBlank(message = "Tiêu đề ghi chú không được bỏ trống!")
    private String title;

    @NotBlank(message = "Nội dung ghi chú không được bỏ trống!")
    private String content;
}


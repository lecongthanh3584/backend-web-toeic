package com.backend.spring.payload.request;

import com.backend.spring.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GrammarContentRequest {
    private Integer contentId;

    @NotNull(message = "Id của ngữ pháp phải có giá trị!")
    private Integer grammarId;

    @NotBlank(message = "Tiêu đề nội dung không được để trống!")
    private String title;

    @NotBlank(message = "Nội dung không được trống!")
    private String content;

    private Integer grammarContentStatus = EStatus.ENABLE.getValue();
}

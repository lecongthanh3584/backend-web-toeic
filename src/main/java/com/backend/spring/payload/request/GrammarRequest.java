package com.backend.spring.payload.request;

import com.backend.spring.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GrammarRequest {
    private Integer grammarId;

    @NotBlank(message = "Tên ngữ pháp không được bỏ trống!")
    private String grammarName;

    private Integer grammarStatus = EStatus.ENABLE.getValue();
}

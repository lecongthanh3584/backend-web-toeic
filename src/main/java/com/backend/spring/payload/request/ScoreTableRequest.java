package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScoreTableRequest {
    private Integer scoreTableId;

    @NotNull(message = "Số câu đúng không được bỏ trống!")
    private Integer numCorrectAnswers;

    @NotNull(message = "Số điểm không được bỏ trống!")
    private Integer score;

    @NotNull(message = "Loại phần thi không được bỏ trống!")
    private Integer type;
}

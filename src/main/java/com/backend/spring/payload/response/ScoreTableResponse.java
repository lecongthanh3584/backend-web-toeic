package com.backend.spring.payload.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ScoreTableResponse {
    private Integer scoreTableId;

    private Integer numCorrectAnswers;

    private Integer score;

    private Integer type; // 0 - listening, 1 - reading
}

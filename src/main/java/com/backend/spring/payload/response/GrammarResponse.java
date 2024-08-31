package com.backend.spring.payload.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrammarResponse {
    private Integer grammarId;

    private String grammarName;

    private Integer grammarStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

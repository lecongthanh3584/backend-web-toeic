package com.backend.spring.payload.response;

import com.backend.spring.entities.Grammar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrammarContentResponse {
    private Integer contentId;

    private String title;

    private String content;

    private Integer grammarContentStatus = 1;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Grammar grammar;
}

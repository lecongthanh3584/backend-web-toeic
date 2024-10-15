package com.backend.spring.payload.response;

import com.backend.spring.entities.Vocabulary;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVocabularyResponse {
    private Integer userVocabularyId;

    private Vocabulary vocabulary;
}

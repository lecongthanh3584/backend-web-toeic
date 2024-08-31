package com.backend.spring.payload.response;

import com.backend.spring.entity.User;
import com.backend.spring.entity.Vocabulary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVocabularyResponse {
    private Integer userVocabularyId;

    private Vocabulary vocabulary;
}

package com.backend.spring.payload.response;

import com.backend.spring.entities.Topic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyResponse {
    private Integer vocabularyId;

    private String word;

    private String ipa;

    private String meaning;

    private String exampleSentence;

    private String image;

    private Integer vocabularyStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Topic topic;
}

package com.backend.spring.mapper;

import com.backend.spring.entities.Vocabulary;
import com.backend.spring.payload.response.VocabularyResponse;

public class VocabularyMapper {
    public static VocabularyResponse mapFromEntityToResponse(Vocabulary vocabulary) {
        if(vocabulary == null) {
            return null;
        }

        return new VocabularyResponse(
                vocabulary.getVocabularyId(),
                vocabulary.getWord(),
                vocabulary.getIpa(),
                vocabulary.getMeaning(),
                vocabulary.getExampleSentence(),
                vocabulary.getImage(),
                vocabulary.getVocabularyStatus(),
                vocabulary.getCreatedAt(),
                vocabulary.getUpdatedAt(),
                vocabulary.getTopic()
        );
    }
}

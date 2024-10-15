package com.backend.spring.mapper;

import com.backend.spring.entities.UserVocabulary;
import com.backend.spring.payload.response.UserVocabularyResponse;

public class UserVocabularyMapper {
    public static UserVocabularyResponse mapFromEntityToResponse(UserVocabulary userVocabulary) {
        if(userVocabulary == null) {
            return null;
        }

        return new UserVocabularyResponse(
                userVocabulary.getUserVocabularyId(),
                userVocabulary.getVocabulary()
        );
    }
}

package com.backend.spring.services.UserVocabulary;

import com.backend.spring.entities.UserVocabulary;
import com.backend.spring.payload.request.UserVocabularyRequest;

import java.util.List;

public interface IUserVocabularyService {
    UserVocabulary createUserVocabulary(UserVocabularyRequest userVocabularyRequest);
    List<UserVocabulary> getUserVocabulariesByUserId(Integer userId);
    boolean deleteUserVocabulary(Integer userVocabularyId);
}

package com.backend.spring.service.UserVocabulary;

import com.backend.spring.entity.UserVocabulary;
import com.backend.spring.payload.request.UserVocabularyRequest;

import java.util.List;

public interface IUserVocabularyService {
    UserVocabulary createUserVocabulary(UserVocabularyRequest userVocabularyRequest);
    List<UserVocabulary> getUserVocabulariesByUserId(Integer userId);
    boolean deleteUserVocabulary(Integer userVocabularyId);
}

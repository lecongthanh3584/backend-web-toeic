package com.backend.spring.service.UserVocabulary;

import com.backend.spring.entity.*;
import com.backend.spring.payload.request.UserVocabularyRequest;
import com.backend.spring.repository.VocabularyRepository;
import com.backend.spring.repository.UserVocabularyRepository;
import com.backend.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserVocabularyService implements IUserVocabularyService {

    @Autowired
    private UserVocabularyRepository userVocabularyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Override
    public UserVocabulary createUserVocabulary(UserVocabularyRequest userVocabularyRequest) {
        Optional<User> userOptional = userRepository.findById(userVocabularyRequest.getUserId());
        Optional<Vocabulary> vocabularyOptional = vocabularyRepository.findById(userVocabularyRequest.getVocabularyId());

        if (userOptional.isPresent() && vocabularyOptional.isPresent()) {

            UserVocabulary userVocabulary = new UserVocabulary();

            userVocabulary.setUser(userOptional.get());
            userVocabulary.setVocabulary(vocabularyOptional.get());

            return userVocabularyRepository.save(userVocabulary);
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserVocabulary> getUserVocabulariesByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            return userVocabularyRepository.findByUser(user);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean deleteUserVocabulary(Integer userVocabularyId) {
        Optional<UserVocabulary> userVocabularyOptional = userVocabularyRepository.findById(userVocabularyId);
        if(userVocabularyOptional.isEmpty()) {
            return false;
        }

        userVocabularyRepository.deleteById(userVocabularyId);
        return true;
    }

}


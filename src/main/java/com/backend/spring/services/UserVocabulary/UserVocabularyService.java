package com.backend.spring.services.UserVocabulary;

import com.backend.spring.entities.*;
import com.backend.spring.payload.request.UserVocabularyRequest;
import com.backend.spring.repositories.VocabularyRepository;
import com.backend.spring.repositories.UserVocabularyRepository;
import com.backend.spring.repositories.UserRepository;
import com.backend.spring.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserVocabularyService implements IUserVocabularyService {

    private final UserVocabularyRepository userVocabularyRepository;

    private final UserRepository userRepository;

    private final VocabularyRepository vocabularyRepository;

    @Override
    public UserVocabulary createUserVocabulary(UserVocabularyRequest userVocabularyRequest) {
        User userLogin = UserUtil.getDataUserLogin();
        Optional<Vocabulary> vocabularyOptional = vocabularyRepository.findById(userVocabularyRequest.getVocabularyId());

        if (userLogin != null && vocabularyOptional.isPresent()) {

            UserVocabulary userVocabulary = new UserVocabulary();

            userVocabulary.setUser(userLogin);
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


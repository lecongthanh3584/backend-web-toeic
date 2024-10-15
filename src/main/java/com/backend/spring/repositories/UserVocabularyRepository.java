package com.backend.spring.repositories;

import com.backend.spring.entities.User;
import com.backend.spring.entities.UserVocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVocabularyRepository extends JpaRepository<UserVocabulary, Integer> {
    List<UserVocabulary> findByUser(User user);

//    void deleteByVocabulary_VocabularyId(Long vocabularyId);
//    void deleteByVocabulary_VocabularyIdAndUserId(Integer vocabularyId, Integer userId);

}


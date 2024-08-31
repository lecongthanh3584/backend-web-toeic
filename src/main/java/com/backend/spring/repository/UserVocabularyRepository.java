package com.backend.spring.repository;

import com.backend.spring.entity.Topic;
import com.backend.spring.entity.Vocabulary;
import com.backend.spring.entity.User;
import com.backend.spring.entity.UserVocabulary;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVocabularyRepository extends JpaRepository<UserVocabulary, Integer> {
    List<UserVocabulary> findByUser(User user);

//    void deleteByVocabulary_VocabularyId(Long vocabularyId);
//    void deleteByVocabulary_VocabularyIdAndUserId(Integer vocabularyId, Integer userId);

}


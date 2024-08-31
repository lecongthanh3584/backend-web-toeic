package com.backend.spring.repository;

import com.backend.spring.entity.Topic;
import com.backend.spring.entity.VocabularyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyQuestionRepository extends JpaRepository<VocabularyQuestion, Integer> {
    List<VocabularyQuestion> findByTopic(Topic topic);
}


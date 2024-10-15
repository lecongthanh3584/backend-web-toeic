package com.backend.spring.repositories;

import com.backend.spring.entities.Topic;
import com.backend.spring.entities.VocabularyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyQuestionRepository extends JpaRepository<VocabularyQuestion, Integer> {
    List<VocabularyQuestion> findByTopic(Topic topic);
}


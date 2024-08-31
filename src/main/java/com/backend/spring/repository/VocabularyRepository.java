package com.backend.spring.repository;
import com.backend.spring.entity.Topic;
import com.backend.spring.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer> {
    List<Vocabulary> findByTopic(Topic topic);
}
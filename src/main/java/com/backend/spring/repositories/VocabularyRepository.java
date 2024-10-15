package com.backend.spring.repositories;
import com.backend.spring.entities.Topic;
import com.backend.spring.entities.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer> {
    List<Vocabulary> findByTopic(Topic topic);
}
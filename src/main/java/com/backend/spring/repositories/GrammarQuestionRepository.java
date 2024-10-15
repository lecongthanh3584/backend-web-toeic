package com.backend.spring.repositories;

import com.backend.spring.entities.GrammarQuestion;
import com.backend.spring.entities.Grammar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarQuestionRepository extends JpaRepository<GrammarQuestion, Integer> {
    List<GrammarQuestion> findByGrammar(Grammar grammar);
}


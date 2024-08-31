package com.backend.spring.repository;

import com.backend.spring.entity.GrammarQuestion;
import com.backend.spring.entity.Grammar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarQuestionRepository extends JpaRepository<GrammarQuestion, Integer> {
    List<GrammarQuestion> findByGrammar(Grammar grammar);
}


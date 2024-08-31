package com.backend.spring.repository;

import com.backend.spring.entity.Grammar;
import com.backend.spring.entity.GrammarContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarContentRepository extends JpaRepository<GrammarContent, Integer> {
    List<GrammarContent> findByGrammar(Grammar grammar);
}

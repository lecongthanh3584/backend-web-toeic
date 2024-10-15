package com.backend.spring.repositories;

import com.backend.spring.entities.Grammar;
import com.backend.spring.entities.GrammarContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarContentRepository extends JpaRepository<GrammarContent, Integer> {
    List<GrammarContent> findByGrammar(Grammar grammar);
}

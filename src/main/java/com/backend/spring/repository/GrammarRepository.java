package com.backend.spring.repository;
import com.backend.spring.entity.Grammar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GrammarRepository extends JpaRepository<Grammar, Integer> {

    boolean existsByGrammarName(String grammarName);

    boolean existsByGrammarNameAndGrammarIdNot(String grammarName, Integer id);
}
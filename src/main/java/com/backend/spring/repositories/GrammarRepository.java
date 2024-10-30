package com.backend.spring.repositories;
import com.backend.spring.entities.Grammar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface GrammarRepository extends JpaRepository<Grammar, Integer> {

    @Query(value = "SELECT * FROM grammar g WHERE g.grammar_name LIKE CONCAT('%', ?1, '%') " +
            "AND g.deleted_at IS NULL", nativeQuery = true)
    Page<Grammar> getAllGrammarWithoutStatus(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM grammar g " +
            "WHERE g.grammar_name LIKE CONCAT('%', ?2, '%') AND g.grammar_status = ?1 " +
            "AND g.deleted_at IS NULL", nativeQuery = true)
    Page<Grammar> getAllGrammarHaveStatus(Integer status, String keyword, Pageable pageable);

    boolean existsByGrammarName(String grammarName);

    boolean existsByGrammarNameAndGrammarIdNot(String grammarName, Integer id);
}
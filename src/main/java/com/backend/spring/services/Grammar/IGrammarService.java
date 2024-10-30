package com.backend.spring.services.Grammar;

import com.backend.spring.entities.Grammar;
import com.backend.spring.payload.request.GrammarRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IGrammarService {
    Page<Grammar> getAllGrammar(Integer pageNumber, String keyword, String... sortBys);
    Page<Grammar> getAllGrammarEnable(Integer pageNumber, String keyword);
    Grammar getGrammarById(Integer id);
    Grammar createGrammar(GrammarRequest grammarRequest);
    Grammar updateGrammar(GrammarRequest grammarRequest);
    Grammar updateGrammarStatus(Integer grammarId, Integer newStatus);
    boolean deleteGrammar(Integer id);
    String getGrammarNameById(Integer id);
    boolean isGrammarNameExists(String grammarName);
}

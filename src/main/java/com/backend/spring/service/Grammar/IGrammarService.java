package com.backend.spring.service.Grammar;

import com.backend.spring.entity.Grammar;
import com.backend.spring.payload.request.GrammarRequest;

import java.util.List;

public interface IGrammarService {
    List<Grammar> getAllGrammar();
    Grammar getGrammarById(Integer id);
    Grammar createGrammar(GrammarRequest grammarRequest);
    Grammar updateGrammar(GrammarRequest grammarRequest);
    Grammar updateGrammarStatus(Integer grammarId, Integer newStatus);
    boolean deleteGrammar(Integer id);
    String getGrammarNameById(Integer id);
    boolean isGrammarNameExists(String grammarName);
}

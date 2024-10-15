package com.backend.spring.mapper;

import com.backend.spring.entities.Grammar;
import com.backend.spring.payload.response.GrammarResponse;

public class GrammarMapper {
    public static GrammarResponse mapFromEntityToResponse(Grammar grammar) {
        if(grammar == null) {
            return null;
        }

        return new GrammarResponse(
                grammar.getGrammarId(),
                grammar.getGrammarName(),
                grammar.getGrammarStatus(),
                grammar.getCreatedAt(),
                grammar.getUpdatedAt()
        );
    }
}

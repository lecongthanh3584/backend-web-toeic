package com.backend.spring.mapper;

import com.backend.spring.entity.GrammarContent;
import com.backend.spring.payload.response.GrammarContentResponse;

public class GrammarContentMapper {
    public static GrammarContentResponse mapFromEntityToResponse(GrammarContent grammarContent) {
        if(grammarContent == null) {
            return null;
        }

        return new GrammarContentResponse(
                grammarContent.getContentId(),
                grammarContent.getTitle(),
                grammarContent.getContent(),
                grammarContent.getGrammarContentStatus(),
                grammarContent.getCreatedAt(),
                grammarContent.getUpdatedAt(),
                grammarContent.getGrammar()
        );
    }
}

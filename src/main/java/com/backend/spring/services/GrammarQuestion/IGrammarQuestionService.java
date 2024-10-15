package com.backend.spring.services.GrammarQuestion;

import com.backend.spring.entities.GrammarQuestion;
import com.backend.spring.payload.request.GrammarQuestionRequest;

import java.util.List;

public interface IGrammarQuestionService {
    GrammarQuestion createGrammarQuestion(GrammarQuestionRequest grammarQuestionRequest);
    GrammarQuestion updateGrammarQuestion(GrammarQuestionRequest grammarQuestionRequest);
    GrammarQuestion updateGrammarQuestionStatus(Integer id, Integer newStatus);
    List<GrammarQuestion> getAllGrammarQuestions();
    GrammarQuestion getGrammarQuestionById(Integer id);
    boolean deleteGrammarQuestion(Integer id);
    List<GrammarQuestion> getGrammarQuestionsByGrammarId(Integer grammarId);
}

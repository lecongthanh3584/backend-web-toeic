package com.backend.spring.services.VocabularyQuestion;

import com.backend.spring.entities.VocabularyQuestion;
import com.backend.spring.payload.request.VocabularyQuestionRequest;

import java.util.List;

public interface IVocabularyQuestionService {
    List<VocabularyQuestion> getAllVocabularyQuestions();
    VocabularyQuestion getVocabularyQuestionById(Integer id);
    VocabularyQuestion createVocabularyQuestion(VocabularyQuestionRequest vocabularyQuestionRequest);
    VocabularyQuestion updateVocabularyQuestion(VocabularyQuestionRequest vocabularyQuestionRequest);
    VocabularyQuestion updateVocabularyQuestionStatus(Integer id, Integer newStatus);
    boolean deleteVocabularyQuestion(Integer id);
    List<VocabularyQuestion> getVocabularyQuestionsByTopicId(Integer topicId);

}

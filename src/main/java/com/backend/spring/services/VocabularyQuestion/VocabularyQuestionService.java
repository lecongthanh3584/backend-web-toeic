package com.backend.spring.services.VocabularyQuestion;

import com.backend.spring.enums.EStatus;
import com.backend.spring.entities.Topic;
import com.backend.spring.entities.VocabularyQuestion;
import com.backend.spring.payload.request.VocabularyQuestionRequest;
import com.backend.spring.repositories.TopicRepository;
import com.backend.spring.repositories.VocabularyQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class VocabularyQuestionService implements IVocabularyQuestionService {

    private final VocabularyQuestionRepository vocabularyQuestionRepository;

    private final TopicRepository topicRepository;

    @Override
    public VocabularyQuestion createVocabularyQuestion(VocabularyQuestionRequest vocabularyQuestionRequest) {
        Optional<Topic> topicOptional = topicRepository.findById(vocabularyQuestionRequest.getTopicId());

        if (topicOptional.isPresent()) {
            VocabularyQuestion vocabularyQuestion = new VocabularyQuestion();

            vocabularyQuestion.setTopic(topicOptional.get());
            vocabularyQuestion.setQuestionContent(vocabularyQuestionRequest.getQuestionContent());
            vocabularyQuestion.setOptionA(vocabularyQuestionRequest.getOptionA());
            vocabularyQuestion.setOptionB(vocabularyQuestionRequest.getOptionB());
            vocabularyQuestion.setOptionC(vocabularyQuestionRequest.getOptionC());
            vocabularyQuestion.setOptionD(vocabularyQuestionRequest.getOptionD());
            vocabularyQuestion.setCorrectOption(vocabularyQuestionRequest.getCorrectOption());
            vocabularyQuestion.setQuestionExplanation(vocabularyQuestionRequest.getQuestionExplanation());
            vocabularyQuestion.setQuestionStatus(EStatus.ENABLE.getValue());

            vocabularyQuestion.setCreatedAt(LocalDateTime.now());
            vocabularyQuestion.setUpdatedAt(LocalDateTime.now());

            return vocabularyQuestionRepository.save(vocabularyQuestion);
        }

        return null;
    }

    @Override
    public VocabularyQuestion updateVocabularyQuestion(VocabularyQuestionRequest vocabularyQuestionRequest) {
        Optional<VocabularyQuestion> vocabularyQuestionOptional = vocabularyQuestionRepository.findById(vocabularyQuestionRequest.getQuestionId());
        Optional<Topic> topicOptional = topicRepository.findById(vocabularyQuestionRequest.getTopicId());

        if (vocabularyQuestionOptional.isPresent() && topicOptional.isPresent()) {

            VocabularyQuestion vocabularyQuestion = vocabularyQuestionOptional.get();

            vocabularyQuestion.setTopic(topicOptional.get());
            vocabularyQuestion.setQuestionContent(vocabularyQuestionRequest.getQuestionContent());
            vocabularyQuestion.setOptionA(vocabularyQuestionRequest.getOptionA());
            vocabularyQuestion.setOptionB(vocabularyQuestionRequest.getOptionB());
            vocabularyQuestion.setOptionC(vocabularyQuestionRequest.getOptionC());
            vocabularyQuestion.setOptionD(vocabularyQuestionRequest.getOptionD());
            vocabularyQuestion.setCorrectOption(vocabularyQuestionRequest.getCorrectOption());
            vocabularyQuestion.setQuestionExplanation(vocabularyQuestionRequest.getQuestionExplanation());
            vocabularyQuestion.setQuestionStatus(vocabularyQuestionRequest.getQuestionStatus());

            vocabularyQuestion.setUpdatedAt(LocalDateTime.now());

            return vocabularyQuestionRepository.save(vocabularyQuestion);
        }

        return null;
    }

    @Override
    public VocabularyQuestion updateVocabularyQuestionStatus(Integer id, Integer newStatus) {
        Optional<VocabularyQuestion> vocabularyQuestionOptional = vocabularyQuestionRepository.findById(id);
        if(vocabularyQuestionOptional.isEmpty()) {
            return null;
        }

        VocabularyQuestion vocabularyQuestionUpdate = vocabularyQuestionOptional.get();

        if(newStatus.equals(EStatus.ENABLE.getValue())) {
            vocabularyQuestionUpdate.setQuestionStatus(newStatus);
        } else if(newStatus.equals(EStatus.DISABLE.getValue())) {
            vocabularyQuestionUpdate.setQuestionStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Invalid status value!");
        }

        vocabularyQuestionUpdate.setUpdatedAt(LocalDateTime.now());

        return vocabularyQuestionRepository.save(vocabularyQuestionUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VocabularyQuestion> getAllVocabularyQuestions() {
        return vocabularyQuestionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public VocabularyQuestion getVocabularyQuestionById(Integer id) {
        return vocabularyQuestionRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteVocabularyQuestion(Integer id) {
        Optional<VocabularyQuestion> vocabularyQuestionOptional = vocabularyQuestionRepository.findById(id);
        if(vocabularyQuestionOptional.isEmpty()) {
            return false;
        }

        vocabularyQuestionRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VocabularyQuestion> getVocabularyQuestionsByTopicId(Integer topicId) {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);

        if (topicOptional.isPresent()) {
            Topic topic = topicOptional.get();

            return vocabularyQuestionRepository.findByTopic(topic);
        }

        return Collections.emptyList();
    }
}

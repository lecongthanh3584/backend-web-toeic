package com.backend.spring.service.GrammarQuestion;

import com.backend.spring.enums.EStatus;
import com.backend.spring.entity.Grammar;
import com.backend.spring.entity.GrammarQuestion;
import com.backend.spring.payload.request.GrammarQuestionRequest;
import com.backend.spring.repository.GrammarQuestionRepository;
import com.backend.spring.repository.GrammarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class GrammarQuestionService implements IGrammarQuestionService {

    @Autowired
    private GrammarQuestionRepository grammarQuestionRepository;

    @Autowired
    private GrammarRepository grammarRepository;

    @Override
    public GrammarQuestion createGrammarQuestion(GrammarQuestionRequest grammarQuestionRequest) {
        Optional<Grammar> grammarOptional = grammarRepository.findById(grammarQuestionRequest.getGrammarId());
        if(grammarOptional.isEmpty()) {
            return null;
        }

        GrammarQuestion grammarQuestion = new GrammarQuestion();

        grammarQuestion.setGrammar(grammarOptional.get());
        grammarQuestion.setQuestionContent(grammarQuestionRequest.getQuestionContent());
        grammarQuestion.setOptionA(grammarQuestionRequest.getOptionA());
        grammarQuestion.setOptionB(grammarQuestionRequest.getOptionB());
        grammarQuestion.setOptionC(grammarQuestionRequest.getOptionC());
        grammarQuestion.setOptionD(grammarQuestionRequest.getOptionD());
        grammarQuestion.setCorrectOption(grammarQuestionRequest.getCorrectOption());
        grammarQuestion.setQuestionExplanation(grammarQuestionRequest.getQuestionExplanation());
        grammarQuestion.setQuestionStatus(EStatus.ENABLE.getValue());

        grammarQuestion.setCreatedAt(LocalDateTime.now());
        grammarQuestion.setUpdatedAt(LocalDateTime.now());

        return grammarQuestionRepository.save(grammarQuestion);
    }

    @Override
    public GrammarQuestion updateGrammarQuestion(GrammarQuestionRequest grammarQuestionRequest) {
        Optional<GrammarQuestion> grammarQuestionOptional = grammarQuestionRepository.findById(grammarQuestionRequest.getQuestionId());
        Optional<Grammar> grammarOptional = grammarRepository.findById(grammarQuestionRequest.getGrammarId());

        if(grammarQuestionOptional.isEmpty() || grammarOptional.isEmpty()) {
            return null;
        }

        GrammarQuestion grammarQuestion = grammarQuestionOptional.get();

        grammarQuestion.setGrammar(grammarOptional.get());
        grammarQuestion.setQuestionContent(grammarQuestionRequest.getQuestionContent());
        grammarQuestion.setOptionA(grammarQuestionRequest.getOptionA());
        grammarQuestion.setOptionB(grammarQuestionRequest.getOptionB());
        grammarQuestion.setOptionC(grammarQuestionRequest.getOptionC());
        grammarQuestion.setOptionD(grammarQuestionRequest.getOptionD());
        grammarQuestion.setCorrectOption(grammarQuestionRequest.getCorrectOption());
        grammarQuestion.setQuestionExplanation(grammarQuestionRequest.getQuestionExplanation());
        grammarQuestion.setQuestionStatus(grammarQuestionRequest.getQuestionStatus());

        grammarQuestion.setUpdatedAt(LocalDateTime.now());

        return grammarQuestionRepository.save(grammarQuestion);
    }

    @Override
    public GrammarQuestion updateGrammarQuestionStatus(Integer id, Integer newStatus) {
        Optional<GrammarQuestion> grammarQuestionOptional = grammarQuestionRepository.findById(id);
        if(grammarQuestionOptional.isEmpty()) {
            return null;
        }

        GrammarQuestion grammarQuestionUpdate = grammarQuestionOptional.get();

        if(newStatus.equals(EStatus.ENABLE.getValue())) {
            grammarQuestionUpdate.setQuestionStatus(newStatus);
        } else if(newStatus.equals(EStatus.DISABLE.getValue())) {
            grammarQuestionUpdate.setQuestionStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Invalid status value");
        }

        grammarQuestionUpdate.setUpdatedAt(LocalDateTime.now());

        return grammarQuestionRepository.save(grammarQuestionUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrammarQuestion> getAllGrammarQuestions() {
        return grammarQuestionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public GrammarQuestion getGrammarQuestionById(Integer id) {
        return grammarQuestionRepository.findById(id).orElse(null);
    }


    @Override
    public boolean deleteGrammarQuestion(Integer id) {
        Optional<GrammarQuestion> grammarQuestionOptional = grammarQuestionRepository.findById(id);
        if(grammarQuestionOptional.isEmpty()) {
            return false;
        }

        grammarQuestionRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrammarQuestion> getGrammarQuestionsByGrammarId(Integer grammarId) {
        Optional<Grammar> grammarOptional = grammarRepository.findById(grammarId);
        if (grammarOptional.isPresent()) {
            Grammar grammar = grammarOptional.get();

            return grammarQuestionRepository.findByGrammar(grammar);
        }

        return Collections.emptyList();
    }
}

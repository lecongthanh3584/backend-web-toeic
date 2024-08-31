package com.backend.spring.service.Grammar;

import com.backend.spring.enums.EStatus;
import com.backend.spring.entity.Grammar;
import com.backend.spring.payload.request.GrammarRequest;
import com.backend.spring.repository.GrammarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class GrammarService implements IGrammarService {

    @Autowired
    private GrammarRepository grammarRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Grammar> getAllGrammar() {
        return grammarRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Grammar getGrammarById(Integer id) {
        return grammarRepository.findById(id).orElse(null);
    }

    @Override
    public Grammar createGrammar(GrammarRequest grammarRequest) {
        Grammar grammar = new Grammar();

        grammar.setGrammarName(grammarRequest.getGrammarName());
        grammar.setCreatedAt(LocalDateTime.now());
        grammar.setUpdatedAt(LocalDateTime.now());
        grammar.setGrammarStatus(EStatus.ENABLE.getValue());

        return grammarRepository.save(grammar);
    }

    @Override
    public Grammar updateGrammar(GrammarRequest grammarRequest) {
        Optional<Grammar> grammarOptional = grammarRepository.findById(grammarRequest.getGrammarId());
        if(grammarOptional.isEmpty()) {
            return null;
        }

        Grammar existingGrammar = grammarOptional.get();
        existingGrammar.setGrammarName(grammarRequest.getGrammarName());
        existingGrammar.setGrammarStatus(grammarRequest.getGrammarStatus());
        existingGrammar.setUpdatedAt(LocalDateTime.now());

        return grammarRepository.save(existingGrammar);
    }

    @Override
    public Grammar updateGrammarStatus(Integer grammarId, Integer newStatus) {
        Optional<Grammar> grammarOptional = grammarRepository.findById(grammarId);
        if(grammarOptional.isEmpty()) {
            return null;
        }

        Grammar grammarUpdate = grammarOptional.get();

        if(newStatus.equals(EStatus.ENABLE.getValue())) {
            grammarUpdate.setGrammarStatus(newStatus);
        } else if(newStatus.equals(EStatus.DISABLE.getValue())) {
            grammarUpdate.setGrammarStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Invalid status value");
        }

        grammarUpdate.setUpdatedAt(LocalDateTime.now());

        return grammarRepository.save(grammarUpdate);
    }

    @Override
    public boolean deleteGrammar(Integer id) {
        Optional<Grammar> grammarOptional = grammarRepository.findById(id);
        if(grammarOptional.isEmpty()) {
            return false;
        }

        grammarRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public String getGrammarNameById(Integer id) {
        Optional<Grammar> grammarOptional = grammarRepository.findById(id);
        return grammarOptional.map(Grammar::getGrammarName).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isGrammarNameExists(String grammarName) {
        return grammarRepository.existsByGrammarName(grammarName);
    }
}

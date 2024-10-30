package com.backend.spring.services.Grammar;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.entities.Grammar;
import com.backend.spring.payload.request.GrammarRequest;
import com.backend.spring.repositories.GrammarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class GrammarService implements IGrammarService {

    private final GrammarRepository grammarRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Grammar> getAllGrammar(Integer pageNumber, String keyword, String... sortBys) {

        List<Sort.Order> orders = getListSort(sortBys);

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(orders));

        return grammarRepository.getAllGrammarWithoutStatus(keyword, pageable);
    }

    @Override
    public Page<Grammar> getAllGrammarEnable(Integer pageNumber, String keyword) {

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "created_at"));

        return grammarRepository.getAllGrammarHaveStatus(EStatus.ACTIVATE.getValue(), keyword, pageable);
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

    private List<Sort.Order> getListSort(String... sortBys) {
        List<Sort.Order> orders = new ArrayList<>();

        for(String sortBy : sortBys) {
            String[] sort = sortBy.split(":"); //Tách từng phần để xác định xem là sắp xếp tăng dần hay giảm dần

            if (sort.length == 2) {
                String field = sort[0].trim();
                String direction = sort[1].trim();

                if (direction.equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, field));
                } else if (direction.equalsIgnoreCase("desc")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, field));
                }
            } else {
                throw new RuntimeException(MessageConstant.INVALID_PARAMETER);
            }
        }

        return orders;
    }
}

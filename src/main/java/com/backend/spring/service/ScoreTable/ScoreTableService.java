package com.backend.spring.service.ScoreTable;

import com.backend.spring.enums.ESection;
import com.backend.spring.entity.ScoreTable;
import com.backend.spring.payload.request.ScoreTableRequest;
import com.backend.spring.repository.ScoreTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScoreTableService implements IScoreTableService {

    @Autowired
    private ScoreTableRepository scoreTableRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ScoreTable> getAllScores() {
        return scoreTableRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ScoreTable getScoreById(Integer id) {
        return scoreTableRepository.findById(id).orElse(null);
    }

    @Override
    public ScoreTable createScore(ScoreTableRequest scoreTableRequest) {
        ScoreTable scoreTable = new ScoreTable();

        scoreTable.setNumCorrectAnswers(scoreTableRequest.getNumCorrectAnswers());
        scoreTable.setScore(scoreTableRequest.getScore());

        if(scoreTableRequest.getType().equals(ESection.LISTENING.getValue())) {
            scoreTable.setType(scoreTableRequest.getType());
        } else if (scoreTableRequest.getType().equals(ESection.READING.getValue())) {
            scoreTable.setType(scoreTableRequest.getType());
        } else {
            throw new IllegalArgumentException("Invalid type value");
        }

        return scoreTableRepository.save(scoreTable);
    }

    @Override
    public ScoreTable updateScore(ScoreTableRequest scoreTableRequest) {
        Optional<ScoreTable> scoreTableOptional = scoreTableRepository.findById(scoreTableRequest.getScoreTableId());
        if (scoreTableOptional.isPresent()) {
            ScoreTable existingScoreTable = scoreTableOptional.get();
            existingScoreTable.setScore(scoreTableRequest.getScore());

            return scoreTableRepository.save(existingScoreTable);
        }

        return null;
    }

    @Override
    public boolean deleteScore(Integer id) {
        Optional<ScoreTable> scoreTableOptional = scoreTableRepository.findById(id);
        if(scoreTableOptional.isEmpty()) {
            return false;
        }

        scoreTableRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoreTable> getListeningScores() {
        return scoreTableRepository.findByType(ESection.LISTENING.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoreTable> getReadingScores() {
        return scoreTableRepository.findByType(ESection.READING.getValue());
    }

}

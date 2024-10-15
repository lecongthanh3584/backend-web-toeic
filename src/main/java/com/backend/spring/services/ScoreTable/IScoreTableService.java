package com.backend.spring.services.ScoreTable;

import com.backend.spring.entities.ScoreTable;
import com.backend.spring.payload.request.ScoreTableRequest;

import java.util.List;

public interface IScoreTableService {
    List<ScoreTable> getAllScores();
    ScoreTable getScoreById(Integer id);
    ScoreTable createScore(ScoreTableRequest scoreTableRequest);
    ScoreTable updateScore(ScoreTableRequest scoreTableRequest);
    boolean deleteScore(Integer id);
    List<ScoreTable> getListeningScores();
    List<ScoreTable> getReadingScores();

}

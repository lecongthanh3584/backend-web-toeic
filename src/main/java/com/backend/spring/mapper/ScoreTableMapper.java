package com.backend.spring.mapper;

import com.backend.spring.entities.ScoreTable;
import com.backend.spring.payload.response.ScoreTableResponse;

public class ScoreTableMapper {
    public static ScoreTableResponse mapFromEntityToResponse(ScoreTable scoreTable) {
        if(scoreTable == null) {
            return null;
        }

        return new ScoreTableResponse(
                scoreTable.getScoreTableId(),
                scoreTable.getNumCorrectAnswers(),
                scoreTable.getScore(),
                scoreTable.getType()
        );
    }
}

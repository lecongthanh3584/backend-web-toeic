package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.ScoreTableMapper;
import com.backend.spring.entities.ScoreTable;
import com.backend.spring.payload.request.ScoreTableRequest;
import com.backend.spring.payload.response.ScoreTableResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.ScoreTable.IScoreTableService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class ScoreTableController {

    private final IScoreTableService iScoreTableService;

    //admin
    @GetMapping("/admin/score-table/get-by-id/{id}")
    public ResponseEntity<?> getScoreById(@PathVariable("id") @Min(1) Integer id) {
        ScoreTableResponse scoreTable = ScoreTableMapper.mapFromEntityToResponse(iScoreTableService.getScoreById(id));

        if (scoreTable != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.ScoreTable.GET_DATA_SUCCESS, scoreTable),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.ScoreTable.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/score-table/create")
    public ResponseEntity<?> createScore(@RequestBody @Valid ScoreTableRequest scoreTableRequest) {
        ScoreTable createdScore = iScoreTableService.createScore(scoreTableRequest);

        if(createdScore != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.ScoreTable.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.ScoreTable.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/score-table/update")
    public ResponseEntity<?> updateScore(@RequestBody @Valid ScoreTableRequest scoreTableRequest) {
        ScoreTable updatedScore = iScoreTableService.updateScore(scoreTableRequest);

        if (updatedScore != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.ScoreTable.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.ScoreTable.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/score-table/delete/{id}")
    public ResponseEntity<?> deleteScore(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iScoreTableService.deleteScore(id);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.ScoreTable.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.ScoreTable.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    //user
    @GetMapping("/public/score-table/get-all")
    public ResponseEntity<?> getAllScores() {
        List<ScoreTableResponse> scoreList = iScoreTableService.getAllScores().stream().map(
                ScoreTableMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.ScoreTable.GET_DATA_SUCCESS, scoreList),
                HttpStatus.OK);
    }

    @GetMapping("/public/score-table/get-listening-scores")
    public ResponseEntity<?> getListeningScores() {
        List<ScoreTableResponse> listeningScores = iScoreTableService.getListeningScores().stream().map(
                ScoreTableMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.ScoreTable.GET_DATA_SUCCESS, listeningScores),
                HttpStatus.OK);
    }

    @GetMapping("/public/score-table/get-reading-scores")
    public ResponseEntity<?> getReadingScores() {
        List<ScoreTableResponse> readingScores = iScoreTableService.getReadingScores().stream().map(
                ScoreTableMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.ScoreTable.GET_DATA_SUCCESS, readingScores),
                HttpStatus.OK);
    }

}

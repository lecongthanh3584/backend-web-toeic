package com.backend.spring.controller;

import com.backend.spring.mapper.ScoreTableMapper;
import com.backend.spring.entity.ScoreTable;
import com.backend.spring.payload.request.ScoreTableRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.ScoreTableResponse;
import com.backend.spring.service.ScoreTable.IScoreTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class ScoreTableController {

    @Autowired
    private IScoreTableService iScoreTableService;

    @GetMapping("/public/score-table/get-all")
    public ResponseEntity<List<ScoreTableResponse>> getAllScores() {
        List<ScoreTableResponse> scoreList = iScoreTableService.getAllScores().stream().map(
                ScoreTableMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(scoreList, HttpStatus.OK);
    }

    @GetMapping("/admin/score-table/get-by-id/{id}")
    public ResponseEntity<ScoreTableResponse> getScoreById(@PathVariable Integer id) {
        ScoreTableResponse scoreTable = ScoreTableMapper.mapFromEntityToResponse(iScoreTableService.getScoreById(id));

        if (scoreTable != null) {
            return new ResponseEntity<>(scoreTable, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/score-table/create")
    public ResponseEntity<MessageResponse> createScore(@RequestBody @Valid ScoreTableRequest scoreTableRequest) {
        ScoreTable createdScore = iScoreTableService.createScore(scoreTableRequest);

        if(createdScore != null) {
            return ResponseEntity.ok(new MessageResponse("Thêm điểm thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Thêm điểm thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/score-table/update")
    public ResponseEntity<MessageResponse> updateScore(@RequestBody @Valid ScoreTableRequest scoreTableRequest) {
        ScoreTable updatedScore = iScoreTableService.updateScore(scoreTableRequest);

        if (updatedScore != null) {
            return ResponseEntity.ok(new MessageResponse("Cập nhật điểm thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật điểm thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/score-table/delete/{id}")
    public ResponseEntity<MessageResponse> deleteScore(@PathVariable Integer id) {
        boolean result = iScoreTableService.deleteScore(id);

        if(result) {
            return ResponseEntity.ok(new MessageResponse("Xóa điểm thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Xoá điểm thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/public/score-table/get-listening-scores")
    public ResponseEntity<List<ScoreTableResponse>> getListeningScores() {
        List<ScoreTableResponse> listeningScores = iScoreTableService.getListeningScores().stream().map(
                ScoreTableMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(listeningScores, HttpStatus.OK);
    }

    @GetMapping("/public/score-table/get-reading-scores")
    public ResponseEntity<List<ScoreTableResponse>> getReadingScores() {
        List<ScoreTableResponse> readingScores = iScoreTableService.getReadingScores().stream().map(
                ScoreTableMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(readingScores, HttpStatus.OK);
    }

}

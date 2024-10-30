package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.VocabularyMapper;
import com.backend.spring.entities.Vocabulary;
import com.backend.spring.payload.request.VocabularyRequest;
import com.backend.spring.payload.response.VocabularyResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.Vocabulary.IVocabularyService;
import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class VocabularyController {

    private final IVocabularyService iVocabularyService;

    //Admin
    @GetMapping("/admin/vocabulary/get-all")
    public ResponseEntity<?> getAllVocabularies() {
        List<VocabularyResponse> vocabularyList = iVocabularyService.getAllVocabularies().stream().map(
                VocabularyMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Vocabulary.GET_DATA_SUCCESS, vocabularyList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/vocabulary/get-by-id/{id}")
    public ResponseEntity<?> getVocabularyById(@PathVariable("id") @Min(1) Integer vocabularyId) {
        VocabularyResponse vocabulary = VocabularyMapper.mapFromEntityToResponse(iVocabularyService.getVocabularyById(vocabularyId));

        if (vocabulary != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Vocabulary.GET_DATA_SUCCESS, vocabulary),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Vocabulary.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    // Lấy danh sách từ vựng theo topic_id (ADMIN)
    @GetMapping("/admin/vocabulary/get-by-topic/{topicId}")
    public ResponseEntity<?> getVocabulariesByTopicId(@PathVariable @Min(1) Integer topicId) {
        List<VocabularyResponse> vocabularyList = iVocabularyService.getVocabulariesByTopicId(topicId).stream().map(
                VocabularyMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!vocabularyList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Vocabulary.GET_DATA_SUCCESS, vocabularyList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Vocabulary.DATA_NOT_FOUND, vocabularyList),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/vocabulary/create")
    public ResponseEntity<?> createVocabulary(@ModelAttribute @Valid VocabularyRequest vocabularyRequest) {
        try {
            Vocabulary createdVocabulary = iVocabularyService.createVocabulary(vocabularyRequest);

            if(createdVocabulary != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Vocabulary.CREATE_SUCCESS),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Vocabulary.CREATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException | java.io.IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/vocabulary/update")
    public ResponseEntity<?> updateVocabulary(@ModelAttribute @Valid VocabularyRequest vocabularyRequest) {
        try {
            Vocabulary updatedVocabulary = iVocabularyService.updateVocabulary(vocabularyRequest);

            if (updatedVocabulary != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Vocabulary.UPDATE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Vocabulary.UPDATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (IOException | java.io.IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/vocabulary/delete/{id}")
    public ResponseEntity<?> deleteVocabulary(@PathVariable("id") @Min(1) Integer vocabularyId) {
        try {
            boolean result = iVocabularyService.deleteVocabulary(vocabularyId);

            if(result) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.Vocabulary.DELETE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.Vocabulary.DELETE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (IOException | java.io.IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/vocabulary/update-status/{id}")
    public ResponseEntity<?> updateVocabularyStatus(@PathVariable("id") @Min(1) Integer vocabularyId, @RequestBody Integer newStatus) {
        Vocabulary vocabularyUpdate = iVocabularyService.updateVocabularyStatus(vocabularyId, newStatus);

        if(vocabularyUpdate != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Vocabulary.UPDATE_STATUS_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Vocabulary.UPDATE_STATUS_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/vocabulary/download-template")
    public ResponseEntity<Resource> downloadTemplate() {
        // Tên file mẫu
        String filename = "vocabulary_template.xlsx";

        // Đọc file mẫu từ thư mục tài liệu tĩnh
        Resource resource = new ClassPathResource("/static/export-template/" + filename);

        // Cài đặt tiêu đề và loại dữ liệu trả về
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // Trả về file mẫu dưới dạng tệp tin (Resource)
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @PostMapping("/admin/vocabulary/upload")
    public ResponseEntity<?> uploadVocabularyFromExcel(@RequestParam("file") MultipartFile file, @RequestParam("topicId") @Min(1) Integer topicId) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPLOAD_FILE_FAILED.getValue(), MessageConstant.Vocabulary.FILE_IS_REQUIRED),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            boolean result = iVocabularyService.uploadVocabularyFromExcel(file, topicId);

            if(result) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPLOAD_FILE_SUCCESS.getValue(), MessageConstant.Vocabulary.UPLOAD_FILE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPLOAD_FILE_FAILED.getValue(), MessageConstant.Vocabulary.UPLOAD_FILE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPLOAD_FILE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //User
    @GetMapping("/public/vocabulary/get-by-topic/{topicId}/enable")
    public ResponseEntity<?> getEnableVocabulariesByTopicId(@PathVariable @Min(1) Integer topicId) {
        List<VocabularyResponse> vocabularyList = iVocabularyService.getVocabulariesByTopicId(topicId).stream().map(
                VocabularyMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các Vocabulary có vocabularyStatus là 1 (ENABLE)
        List<VocabularyResponse> filteredVocabularies = vocabularyList.stream()
                .filter(vocabulary -> vocabulary.getVocabularyStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredVocabularies.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Vocabulary.GET_DATA_SUCCESS, filteredVocabularies),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Vocabulary.DATA_NOT_FOUND, filteredVocabularies),
                    HttpStatus.NOT_FOUND);
        }
    }

}

package com.backend.spring.controller;

import com.backend.spring.enums.EStatus;
import com.backend.spring.mapper.VocabularyMapper;
import com.backend.spring.entity.Vocabulary;
import com.backend.spring.payload.request.VocabularyRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.VocabularyResponse;
import com.backend.spring.service.Vocabulary.IVocabularyService;
import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;
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
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class VocabularyController {

    @Autowired
    private IVocabularyService iVocabularyService;

    @GetMapping("/admin/vocabulary/get-all")
    public ResponseEntity<List<VocabularyResponse>> getAllVocabularies() {
        List<VocabularyResponse> vocabularyList = iVocabularyService.getAllVocabularies().stream().map(
                VocabularyMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(vocabularyList, HttpStatus.OK);
    }

    @GetMapping("/admin/vocabulary/get-by-id/{id}")
    public ResponseEntity<VocabularyResponse> getVocabularyById(@PathVariable("id") Integer vocabularyId) {
        VocabularyResponse vocabulary = VocabularyMapper.mapFromEntityToResponse(iVocabularyService.getVocabularyById(vocabularyId));

        if (vocabulary != null) {
            return new ResponseEntity<>(vocabulary, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Lấy danh sách từ vựng theo topic_id (ADMIN)
    @GetMapping("/admin/vocabulary/get-by-topic/{topicId}")
    public ResponseEntity<List<VocabularyResponse>> getVocabulariesByTopicId(@PathVariable Integer topicId) {
        List<VocabularyResponse> vocabularyList = iVocabularyService.getVocabulariesByTopicId(topicId).stream().map(
                VocabularyMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!vocabularyList.isEmpty()) {
            return new ResponseEntity<>(vocabularyList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(vocabularyList, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/vocabulary/get-by-topic/{topicId}/enable")
    public ResponseEntity<List<VocabularyResponse>> getEnableVocabulariesByTopicId(@PathVariable Integer topicId) {
        List<VocabularyResponse> vocabularyList = iVocabularyService.getVocabulariesByTopicId(topicId).stream().map(
                VocabularyMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các Vocabulary có vocabularyStatus là 1 (ENABLE)
        List<VocabularyResponse> filteredVocabularies = vocabularyList.stream()
                .filter(vocabulary -> vocabulary.getVocabularyStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredVocabularies.isEmpty()) {
            return new ResponseEntity<>(filteredVocabularies, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(filteredVocabularies, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/vocabulary/create")
    public ResponseEntity<MessageResponse> createVocabulary(@ModelAttribute @Valid VocabularyRequest vocabularyRequest) {
        try {
            Vocabulary createdVocabulary = iVocabularyService.createVocabulary(vocabularyRequest);

            if(createdVocabulary != null) {
                return ResponseEntity.ok(new MessageResponse("Thêm từ vựng thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Thêm từ vựng thất bại!"), HttpStatus.BAD_REQUEST);
            }
        } catch (IOException | java.io.IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/vocabulary/update")
    public ResponseEntity<MessageResponse> updateVocabulary(@ModelAttribute @Valid VocabularyRequest vocabularyRequest) {
        try {
            Vocabulary updatedVocabulary = iVocabularyService.updateVocabulary(vocabularyRequest);

            if (updatedVocabulary != null) {
                return ResponseEntity.ok(new MessageResponse("Cập nhật từ vựng thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Cập nhật từ vựng thất bại!"), HttpStatus.BAD_REQUEST);
            }

        } catch (IOException | java.io.IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi khi cập nhật từ vựng: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/vocabulary/delete/{id}")
    public ResponseEntity<MessageResponse> deleteVocabulary(@PathVariable("id") Integer vocabularyId) {
        try {
            boolean result = iVocabularyService.deleteVocabulary(vocabularyId);

            if(result) {
                return ResponseEntity.ok(new MessageResponse("Xóa từ vựng thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Xoá từ vựng thất bại!"), HttpStatus.BAD_REQUEST);
            }

        } catch (IOException | java.io.IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi khi xoá từ vựng: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/vocabulary/update-status/{id}")
    public ResponseEntity<MessageResponse> updateVocabularyStatus(@PathVariable("id") Integer vocabularyId, @RequestBody Integer newStatus) {
        Vocabulary vocabularyUpdate = iVocabularyService.updateVocabularyStatus(vocabularyId, newStatus);

        if(vocabularyUpdate != null) {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái của từ vựng thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái của từ vựng thất bại!"), HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<MessageResponse> uploadVocabularyFromExcel(@RequestParam("file") MultipartFile file, @RequestParam("topicId") Integer topicId) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("Vui lòng chọn 1 file để upload"), HttpStatus.BAD_REQUEST);
        }

        try {
            boolean result = iVocabularyService.uploadVocabularyFromExcel(file, topicId);

            if(result) {
                return ResponseEntity.ok(new MessageResponse("Upload thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Upload thất bại!"), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("Upload thất bại: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

package com.backend.spring.controller;

import com.backend.spring.mapper.TopicMapper;
import com.backend.spring.entity.Topic;
import com.backend.spring.payload.request.TopicRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.TopicResponse;
import com.backend.spring.service.Topic.ITopicService;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class TopicController {

    @Autowired
    private ITopicService iTopicService;

//  Admin
    @GetMapping("/admin/topic/get-all")
    public ResponseEntity<List<TopicResponse>> getAllTopics() {
        List<TopicResponse> topicList = iTopicService.getAllTopics().stream().map(
                TopicMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(topicList, HttpStatus.OK);
    }

//  Người dùng
    @GetMapping("/public/topic/get-all-enable")
    public ResponseEntity<List<TopicResponse>> getAllEnableTopics() {
        List<TopicResponse> topicList = iTopicService.getAllTopicEnable().stream().map(
                TopicMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!topicList.isEmpty()) {
            return new ResponseEntity<>(topicList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(topicList, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/topic/get-by-id/{id}")
    public ResponseEntity<TopicResponse> getTopicById(@PathVariable("id") Integer topicId) {
        TopicResponse topic = TopicMapper.mapFromEntityToResponse(iTopicService.getTopicById(topicId));

        if (topic != null) {
            return new ResponseEntity<>(topic, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/topic/create")
    public ResponseEntity<MessageResponse> createTopic(@ModelAttribute @Valid TopicRequest topicRequest) {
        // Kiểm tra xem tên topic đã tồn tại chưa
        if (iTopicService.isTopicNameExists(topicRequest.getTopicName())) {
            return new ResponseEntity<>(new MessageResponse("Tên chủ đề đã tồn tại, vui lòng chọn tên khác"), HttpStatus.BAD_REQUEST);
        }

        try {
            Topic createdTopic = iTopicService.createTopic(topicRequest);

            if(createdTopic != null) {
                return ResponseEntity.ok(new MessageResponse("Thêm mới topic thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Thêm mới topic thất bại!"), HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/topic/update")
    public ResponseEntity<MessageResponse> updateTopic(@ModelAttribute @Valid TopicRequest topicRequest) {
        // Kiểm tra trùng lặp tên topic (nếu tên đã thay đổi)
        if (iTopicService.isTopicNameExists(topicRequest.getTopicName())) {
            return new ResponseEntity<>(new MessageResponse("Tên chủ đề đã tồn tại, vui lòng chọn tên khác"), HttpStatus.BAD_REQUEST);
        }

        try {
            Topic updatedTopic = iTopicService.updateTopic(topicRequest);

            if (updatedTopic != null) {
                return ResponseEntity.ok(new MessageResponse("Cập nhật topic thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Cập nhật topic thất bại!"), HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/topic/delete/{id}")
    public ResponseEntity<MessageResponse> deleteTopic(@PathVariable("id") Integer topicId) {
        try {
            boolean result = iTopicService.deleteTopic(topicId);

            if(result) {
                return ResponseEntity.ok(new MessageResponse("Xóa topic thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Xoá topic thất bại!"), HttpStatus.BAD_REQUEST);
            }
        }catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/admin/topic/update-status/{id}")
    public ResponseEntity<MessageResponse> updateTopicStatus(@PathVariable("id") Integer topicId, @RequestBody Integer newStatus) {
        Topic topicUpdate = iTopicService.updateTopicStatus(topicId, newStatus);

        if(topicUpdate != null) {
            return new ResponseEntity<>(new MessageResponse("Cập nhật status của topic thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật status của topic thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/topic/download-template")
    public ResponseEntity<Resource> downloadTemplate() {
        // Tên file mẫu
        String filename = "topic_template.xlsx";

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

    @PostMapping("/admin/topic/upload")
    public ResponseEntity<MessageResponse> uploadTopicsFromExcel(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("Vui lòng chọn file để upload."), HttpStatus.BAD_REQUEST);
        }

        try {
            iTopicService.uploadTopicFromExcel(file);
            return ResponseEntity.ok(new MessageResponse("Upload thành công!"));

        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("Upload thất bại: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

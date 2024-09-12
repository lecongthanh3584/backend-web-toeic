package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.TopicMapper;
import com.backend.spring.entity.Topic;
import com.backend.spring.payload.request.TopicRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.TopicResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.Topic.ITopicService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
@Validated
public class TopicController {

    @Autowired
    private ITopicService iTopicService;

//  Admin
    @GetMapping("/admin/topic/get-all")
    public ResponseEntity<?> getAllTopics() {
        List<TopicResponse> topicList = iTopicService.getAllTopics().stream().map(
                TopicMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Topic.GET_DATA_SUCCESS, topicList),
                HttpStatus.OK);
    }

//  Người dùng
    @GetMapping("/public/topic/get-all/enable")
    public ResponseEntity<?> getAllEnableTopics() {
        List<TopicResponse> topicList = iTopicService.getAllTopicEnable().stream().map(
                TopicMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!topicList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Topic.GET_DATA_SUCCESS, topicList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Topic.DATA_NOT_FOUND, topicList),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/topic/get-by-id/{id}")
    public ResponseEntity<?> getTopicById(@PathVariable("id") @Min(1) Integer topicId) {
        TopicResponse topic = TopicMapper.mapFromEntityToResponse(iTopicService.getTopicById(topicId));

        if (topic != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Topic.GET_DATA_SUCCESS, topic),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Topic.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/topic/create")
    public ResponseEntity<?> createTopic(@ModelAttribute @Valid TopicRequest topicRequest) {
        // Kiểm tra xem tên topic đã tồn tại chưa
        if (iTopicService.isTopicNameExists(topicRequest.getTopicName())) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Topic.TOPIC_NAME_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            Topic createdTopic = iTopicService.createTopic(topicRequest);

            if(createdTopic != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Topic.CREATE_SUCCESS),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Topic.CREATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/topic/update")
    public ResponseEntity<?> updateTopic(@ModelAttribute @Valid TopicRequest topicRequest) {
        // Kiểm tra trùng lặp tên topic (nếu tên đã thay đổi)
        if (iTopicService.isTopicNameExistsAndIdNot(topicRequest.getTopicName(), topicRequest.getTopicId())) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Topic.TOPIC_NAME_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            Topic updatedTopic = iTopicService.updateTopic(topicRequest);

            if (updatedTopic != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Topic.UPDATE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Topic.UPDATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/topic/delete/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable("id") @Min(1) Integer topicId) {
        try {
            boolean result = iTopicService.deleteTopic(topicId);

            if(result) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.Topic.DELETE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.Topic.DELETE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        }catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/admin/topic/update-status/{id}")
    public ResponseEntity<?> updateTopicStatus(@PathVariable("id") @Min(1) Integer topicId, @RequestBody Integer newStatus) {
        Topic topicUpdate = iTopicService.updateTopicStatus(topicId, newStatus);

        if(topicUpdate != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Topic.UPDATE_STATUS_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Topic.UPDATE_STATUS_FAILED),
                    HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<?> uploadTopicsFromExcel(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Topic.FILE_IS_REQUIRED),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            iTopicService.uploadTopicFromExcel(file);
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Topic.UPLOAD_FILE_SUCCESS),
                    HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

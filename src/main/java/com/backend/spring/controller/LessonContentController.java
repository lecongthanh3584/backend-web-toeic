package com.backend.spring.controller;

import com.backend.spring.enums.EStatus;
import com.backend.spring.mapper.LessonContentMapper;
import com.backend.spring.entity.LessonContent;
import com.backend.spring.payload.request.LessonContentRequest;
import com.backend.spring.payload.response.LessonContentResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.service.LessonContent.ILessonContentService;
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
public class LessonContentController {

    @Autowired
    private ILessonContentService iLessonContentService;

    @GetMapping("/admin/lesson-content/get-all")
    public ResponseEntity<List<LessonContentResponse>> getAllLessonContents() {
        List<LessonContentResponse> lessonContents = iLessonContentService.getAllLessonContents().stream().map(
                LessonContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(lessonContents, HttpStatus.OK);
    }

    @GetMapping("/public/lesson-content/get-all-enable")
    public ResponseEntity<List<LessonContentResponse>> getAllLessonContentsEnable() {
        List<LessonContentResponse> lessonContents = iLessonContentService.getAllLessonContents().stream().map(
                LessonContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        List<LessonContentResponse> lessonContentResponseEnableList = lessonContents.stream().filter(
                item -> item.getLessonContentStatus().equals(EStatus.ENABLE.getValue())
        ).collect(Collectors.toList());

        return new ResponseEntity<>(lessonContentResponseEnableList, HttpStatus.OK);
    }

    @GetMapping("/admin/lesson-content/get-by-id/{id}")
    public ResponseEntity<LessonContentResponse> getLessonContentById(@PathVariable Integer id) {
        LessonContentResponse lessonContent = LessonContentMapper.mapFromEntityToResponse(iLessonContentService.getLessonContentById(id));

        if (lessonContent != null) {
            return new ResponseEntity<>(lessonContent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/lesson-content/create")
    public ResponseEntity<MessageResponse> createLessonContent(@RequestBody @Valid LessonContentRequest lessonContentRequest) {
        LessonContent createdLessonContent = iLessonContentService.createLessonContent(lessonContentRequest);

        if (createdLessonContent != null) {
            return ResponseEntity.ok(new MessageResponse("Thêm nội dung bài học thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Thêm nội dung bài học thất bại"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/lesson-content/update")
    public ResponseEntity<MessageResponse> updateLessonContent(@RequestBody @Valid LessonContentRequest lessonContentRequest) {
        LessonContent updatedLessonContent = iLessonContentService.updateLessonContent(lessonContentRequest);

        if (updatedLessonContent != null) {
            return ResponseEntity.ok(new MessageResponse("Cập nhật nội dung bài học thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật nội dung bài học thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/lesson-content/delete/{id}")
    public ResponseEntity<MessageResponse> deleteLessonContent(@PathVariable Integer id) {
        boolean result = iLessonContentService.deleteLessonContent(id);

        if(result) {
            return ResponseEntity.ok(new MessageResponse("Xóa nội dung bài học thành công!"));
        }else {
            return new ResponseEntity<>(new MessageResponse("Xoá nội dung bài học thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/lesson-content/update-status/{id}")
    public ResponseEntity<MessageResponse> updateLessonContentStatus(@PathVariable Integer id, @RequestBody Integer newStatus) {
       LessonContent lessonContent = iLessonContentService.updateLessonContentStatus(id, newStatus);

       if(lessonContent != null) {
           return new ResponseEntity<>(new MessageResponse("Cập nhật status cho nội dung bài học thành công!"), HttpStatus.OK);
       } else {
           return new ResponseEntity<>(new MessageResponse("Cập nhật status cho nội dung bài học thất bại!"), HttpStatus.BAD_REQUEST);
       }
    }

    // Lấy danh sách nội dung bài học theo lesson_id
    @GetMapping("/admin/lesson-content/get-content-by-lesson/{lessonId}")
    public ResponseEntity<List<LessonContentResponse>> getLessonContentsByLessonId(@PathVariable Integer lessonId) {
        List<LessonContentResponse> lessonContents = iLessonContentService.getLessonContentsByLessonId(lessonId).stream().map(
                LessonContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!lessonContents.isEmpty()) {
            return new ResponseEntity<>(lessonContents, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(lessonContents, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/lesson-content/get-content-by-lesson/{lessonId}/enable")
    public ResponseEntity<List<LessonContentResponse>> getEnableLessonContentsByLessonId(@PathVariable Integer lessonId) {
        List<LessonContentResponse> lessonContents = iLessonContentService.getLessonContentsByLessonId(lessonId).stream().map(
                LessonContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các LessonContent có lessonContentStatus là 1
        List<LessonContentResponse> filteredLessonContents = lessonContents.stream()
                .filter(content -> content.getLessonContentStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredLessonContents.isEmpty()) {
            return new ResponseEntity<>(filteredLessonContents, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(filteredLessonContents, HttpStatus.NOT_FOUND);
        }
    }

}

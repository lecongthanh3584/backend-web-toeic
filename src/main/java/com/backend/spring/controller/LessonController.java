package com.backend.spring.controller;

import com.backend.spring.enums.EStatus;
import com.backend.spring.mapper.LessonMapper;
import com.backend.spring.entity.Lesson;
import com.backend.spring.payload.request.LessonRequest;
import com.backend.spring.payload.response.LessonResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.service.Lesson.ILessonService;
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
public class LessonController {

    @Autowired
    private ILessonService iLessonService;

    @GetMapping("/admin/lesson/get-all")
    public ResponseEntity<List<LessonResponse>> getAllLessons() {
        List<LessonResponse> lessonList = iLessonService.getAllLessons().stream().map(
                LessonMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(lessonList, HttpStatus.OK);
    }

    @GetMapping("/admin/lesson/get-by-id/{id}")
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable Integer id) {
        LessonResponse lesson = LessonMapper.mapFromEntityToResponse(iLessonService.getLessonById(id));

        if (lesson != null) {
            return new ResponseEntity<>(lesson, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/lesson/get-lesson-name/{id}")
    public ResponseEntity<String> getLessonNameById(@PathVariable Integer id) {
        String lessonName = iLessonService.getLessonNameById(id);

        if (lessonName != null) {
            return new ResponseEntity<>(lessonName, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/lesson/create")
    public ResponseEntity<MessageResponse> createLesson(@RequestBody @Valid LessonRequest lessonRequest) {
        Lesson createdLesson = iLessonService.createLesson(lessonRequest);

        if (createdLesson != null) {
            return ResponseEntity.ok(new MessageResponse("Thêm bài học thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Thêm bài học thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/lesson/update")
    public ResponseEntity<MessageResponse> updateLesson(@RequestBody @Valid LessonRequest lessonRequest) {
        Lesson updatedLesson = iLessonService.updateLesson(lessonRequest);

        if (updatedLesson != null) {
            return ResponseEntity.ok(new MessageResponse("Cập nhật bài học thành công!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật bài học thất bại!"),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/lesson/delete/{id}")
    public ResponseEntity<MessageResponse> deleteLesson(@PathVariable Integer id) {
        boolean result = iLessonService.deleteLesson(id);

        if(result) {
            return new ResponseEntity<>(new MessageResponse("Xóa bài học thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Xóa bài học thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/lesson/update-status/{id}")
    public ResponseEntity<MessageResponse> updateLessonStatus(@PathVariable Integer id, @RequestBody Integer newStatus) {
       Lesson lesson = iLessonService.updateLessonStatus(id, newStatus);

       if(lesson != null) {
           return new ResponseEntity<>(new MessageResponse("Cập nhật status của bài học thành công!"), HttpStatus.OK);
       } else {
           return new ResponseEntity<>(new MessageResponse("Cập nhật status của bài học thất bại!"), HttpStatus.BAD_REQUEST);
       }
    }

    // Lấy danh sách bài học theo section_id (Admin)
    @GetMapping("/admin/lesson/get-lesson-by-section/{sectionId}")
    public ResponseEntity<List<LessonResponse>> getLessonsBySectionId(@PathVariable Integer sectionId) {
        List<LessonResponse> lessonList = iLessonService.getLessonsBySectionId(sectionId).stream().map(
                LessonMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!lessonList.isEmpty()) {
            return new ResponseEntity<>(lessonList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(lessonList, HttpStatus.NOT_FOUND);
        }
    }

    // Lấy danh sách bài học theo section_id (Người dùng)
    @GetMapping("/public/lesson/get-lesson-by-section/{sectionId}/enable")
    public ResponseEntity<List<LessonResponse>> getEnableLessonsBySectionId(@PathVariable Integer sectionId) {
        List<LessonResponse> lessonList = iLessonService.getLessonsBySectionId(sectionId).stream().map(
                LessonMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các bài học có lessonStatus là 1
        List<LessonResponse> filteredLessonList = lessonList.stream()
                .filter(lesson -> lesson.getLessonStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredLessonList.isEmpty()) {
            return new ResponseEntity<>(filteredLessonList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(filteredLessonList, HttpStatus.NOT_FOUND);
        }
    }


}

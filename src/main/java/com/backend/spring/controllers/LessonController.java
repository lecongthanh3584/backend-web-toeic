package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.LessonMapper;
import com.backend.spring.entities.Lesson;
import com.backend.spring.payload.request.LessonRequest;
import com.backend.spring.payload.response.LessonResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.Lesson.ILessonService;
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
public class LessonController {

    private final ILessonService iLessonService;

    @GetMapping("/admin/lesson/get-all")
    public ResponseEntity<?> getAllLessons() {
        List<LessonResponse> lessonList = iLessonService.getAllLessons().stream().map(
                LessonMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Lesson.GET_DATA_SUCCESS, lessonList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/lesson/get-by-id/{id}")
    public ResponseEntity<?> getLessonById(@PathVariable("id") @Min(1) Integer id) {
        LessonResponse lesson = LessonMapper.mapFromEntityToResponse(iLessonService.getLessonById(id));

        if (lesson != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Lesson.GET_DATA_SUCCESS, lesson),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Lesson.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/lesson/get-lesson-name/{id}")
    public ResponseEntity<?> getLessonNameById(@PathVariable("id") @Min(1) Integer id) {
        String lessonName = iLessonService.getLessonNameById(id);

        if (lessonName != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Lesson.GET_DATA_SUCCESS, lessonName),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Lesson.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/lesson/create")
    public ResponseEntity<?> createLesson(@RequestBody @Valid LessonRequest lessonRequest) {
        Lesson createdLesson = iLessonService.createLesson(lessonRequest);

        if (createdLesson != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Lesson.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Lesson.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/lesson/update")
    public ResponseEntity<?> updateLesson(@RequestBody @Valid LessonRequest lessonRequest) {
        Lesson updatedLesson = iLessonService.updateLesson(lessonRequest);

        if (updatedLesson != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Lesson.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Lesson.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/lesson/delete/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iLessonService.deleteLesson(id);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.Lesson.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.Lesson.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/lesson/update-status/{id}")
    public ResponseEntity<?> updateLessonStatus(@PathVariable("id") @Min(1) Integer id, @RequestBody Integer newStatus) {
       Lesson lesson = iLessonService.updateLessonStatus(id, newStatus);

       if(lesson != null) {
           return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Lesson.UPDATE_STATUS_SUCCESS),
                   HttpStatus.OK);
       } else {
           return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Lesson.UPDATE_STATUS_FAILED),
                   HttpStatus.BAD_REQUEST);
       }
    }

    // Lấy danh sách bài học theo section_id (Admin)
    @GetMapping("/admin/lesson/get-lesson-by-section/{sectionId}")
    public ResponseEntity<?> getLessonsBySectionId(@PathVariable("sectionId") @Min(1) Integer sectionId) {
        List<LessonResponse> lessonList = iLessonService.getLessonsBySectionId(sectionId).stream().map(
                LessonMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!lessonList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Lesson.GET_DATA_SUCCESS, lessonList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Lesson.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    // Lấy danh sách bài học theo section_id (Người dùng)
    @GetMapping("/public/lesson/get-lesson-by-section/{sectionId}/enable")
    public ResponseEntity<?> getEnableLessonsBySectionId(@PathVariable("sectionId") @Min(1) Integer sectionId) {
        List<LessonResponse> lessonList = iLessonService.getLessonsBySectionId(sectionId).stream().map(
                LessonMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các bài học có lessonStatus là 1
        List<LessonResponse> filteredLessonList = lessonList.stream()
                .filter(lesson -> lesson.getLessonStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredLessonList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Lesson.GET_DATA_SUCCESS, filteredLessonList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Lesson.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }


}

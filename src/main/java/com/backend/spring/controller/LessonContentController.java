package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.LessonContentMapper;
import com.backend.spring.entity.LessonContent;
import com.backend.spring.payload.request.LessonContentRequest;
import com.backend.spring.payload.response.LessonContentResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.LessonContent.ILessonContentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Validated
public class LessonContentController {

    @Autowired
    private ILessonContentService iLessonContentService;

    @GetMapping("/admin/lesson-content/get-all")
    public ResponseEntity<?> getAllLessonContents() {
        List<LessonContentResponse> lessonContents = iLessonContentService.getAllLessonContents().stream().map(
                LessonContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.LessonContent.GET_DATA_SUCCESS, lessonContents),
                HttpStatus.OK);
    }

    @GetMapping("/public/lesson-content/get-all/enable")
    public ResponseEntity<?> getAllLessonContentsEnable() {
        List<LessonContentResponse> lessonContents = iLessonContentService.getAllLessonContents().stream().map(
                LessonContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        List<LessonContentResponse> lessonContentResponseEnableList = lessonContents.stream().filter(
                item -> item.getLessonContentStatus().equals(EStatus.ENABLE.getValue())
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.LessonContent.GET_DATA_SUCCESS, lessonContentResponseEnableList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/lesson-content/get-by-id/{id}")
    public ResponseEntity<?> getLessonContentById(@PathVariable("id") @Min(1) Integer id) {
        LessonContentResponse lessonContent = LessonContentMapper.mapFromEntityToResponse(iLessonContentService.getLessonContentById(id));

        if (lessonContent != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.LessonContent.GET_DATA_SUCCESS, lessonContent),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.LessonContent.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/lesson-content/create")
    public ResponseEntity<?> createLessonContent(@RequestBody @Valid LessonContentRequest lessonContentRequest) {
        LessonContent createdLessonContent = iLessonContentService.createLessonContent(lessonContentRequest);

        if (createdLessonContent != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.LessonContent.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.LessonContent.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/lesson-content/update")
    public ResponseEntity<?> updateLessonContent(@RequestBody @Valid LessonContentRequest lessonContentRequest) {
        LessonContent updatedLessonContent = iLessonContentService.updateLessonContent(lessonContentRequest);

        if (updatedLessonContent != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.LessonContent.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.LessonContent.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/lesson-content/delete/{id}")
    public ResponseEntity<?> deleteLessonContent(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iLessonContentService.deleteLessonContent(id);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.LessonContent.DELETE_SUCCESS),
                    HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.LessonContent.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/lesson-content/update-status/{id}")
    public ResponseEntity<?> updateLessonContentStatus(@PathVariable("id") @Min(1) Integer id, @RequestBody Integer newStatus) {
       LessonContent lessonContent = iLessonContentService.updateLessonContentStatus(id, newStatus);

       if(lessonContent != null) {
           return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.LessonContent.UPDATE_STATUS_SUCCESS),
                   HttpStatus.OK);
       } else {
           return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.LessonContent.UPDATE_STATUS_FAILED),
                   HttpStatus.BAD_REQUEST);
       }
    }

    // Lấy danh sách nội dung bài học theo lesson_id
    @GetMapping("/admin/lesson-content/get-content-by-lesson/{lessonId}")
    public ResponseEntity<?> getLessonContentsByLessonId(@PathVariable("lessonId") @Min(1) Integer lessonId) {
        List<LessonContentResponse> lessonContents = iLessonContentService.getLessonContentsByLessonId(lessonId).stream().map(
                LessonContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!lessonContents.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.LessonContent.GET_DATA_SUCCESS, lessonContents),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.LessonContent.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/lesson-content/get-content-by-lesson/{lessonId}/enable")
    public ResponseEntity<?> getEnableLessonContentsByLessonId(@PathVariable Integer lessonId) {
        List<LessonContentResponse> lessonContents = iLessonContentService.getLessonContentsByLessonId(lessonId).stream().map(
                LessonContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các LessonContent có lessonContentStatus là 1
        List<LessonContentResponse> filteredLessonContents = lessonContents.stream()
                .filter(content -> content.getLessonContentStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredLessonContents.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.LessonContent.GET_DATA_SUCCESS, filteredLessonContents),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.LessonContent.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

}

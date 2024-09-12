package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.ExamQuestionMapper;
import com.backend.spring.entity.ExamQuestion;
import com.backend.spring.payload.request.ExamQuestionRequest;
import com.backend.spring.payload.response.ExamQuestionResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.ExamQuestion.IExamQuestionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@Validated
public class ExamQuestionController {

    @Autowired
    private IExamQuestionService iExamQuestionService;

    @GetMapping("/admin/exam-question/get-all")
    public ResponseEntity<?> getAllExamQuestions() {
        List<ExamQuestionResponse> examQuestionList = iExamQuestionService.getAllExamQuestions().stream().map(
                ExamQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.ExamQuestion.GET_DATA_SUCCESS,examQuestionList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/exam-question/get-by-id/{id}")
    public ResponseEntity<?> getExamQuestionById(@PathVariable("id") @Min(1) Integer id) {
        ExamQuestionResponse examQuestion = ExamQuestionMapper.mapFromEntityToResponse(iExamQuestionService.getExamQuestionById(id));

        if (examQuestion != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.ExamQuestion.GET_DATA_SUCCESS, examQuestion),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.ExamQuestion.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/exam-question/create")
    public ResponseEntity<?> createExamQuestion(@ModelAttribute @Valid ExamQuestionRequest examQuestionRequest) {
        try {
            ExamQuestion createdExamQuestion = iExamQuestionService.createExamQuestion(examQuestionRequest);

            if(createdExamQuestion != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.ExamQuestion.CREATE_SUCCESS),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.ExamQuestion.CREATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/exam-question/update")
    public ResponseEntity<?> updateExamQuestion(@ModelAttribute @Valid ExamQuestionRequest examQuestionRequest) {
        try {
            ExamQuestion updatedExamQuestion = iExamQuestionService.updateExamQuestion(examQuestionRequest);

            if (updatedExamQuestion != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.ExamQuestion.UPDATE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.ExamQuestion.UPDATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/exam-question/delete/{id}")
    public ResponseEntity<?> deleteExamQuestion(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iExamQuestionService.deleteExamQuestion(id);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.ExamQuestion.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.ExamQuestion.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/exam-question/get-question-by-exam/{examId}")
    public ResponseEntity<?> getQuestionsByExamIdRoleUser(@PathVariable("examId") @Min(1) Integer examId) {
        List<ExamQuestionResponse> examQuestionList = iExamQuestionService.getExamQuestionsByExamId(examId).stream().map(
                ExamQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!examQuestionList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.ExamQuestion.GET_DATA_SUCCESS, examQuestionList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.ExamQuestion.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/exam-question/get-question-by-exam/{examId}")
    public ResponseEntity<?> getQuestionsByExamIdRoleAdmin(@PathVariable("examId") @Min(1) Integer examId) {
        return getQuestionsByExamIdRoleUser(examId);
    }

    @DeleteMapping("/admin/exam-question/delete-question-by-exam/{examId}")
    public ResponseEntity<?> deleteExamQuestionsByExamId(@PathVariable Integer examId) {
        boolean result = iExamQuestionService.deleteAllExamQuestionsByExamId(examId);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.ExamQuestion.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.ExamQuestion.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/admin/exam-question/upload-excel")
    public ResponseEntity<?> uploadExamQuestionsFromExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("examId") Integer examId) {

        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.FILE_REQUIRED.getValue(), MessageConstant.ExamQuestion.FILE_REQUIRED),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            boolean resulst = iExamQuestionService.uploadExamQuestionsFromExcel(file, examId);

            if(resulst) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.ExamQuestion.UPLOAD_FILE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.ExamQuestion.UPLOAD_FILE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPLOAD_FILE_FAILED.getValue(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/admin/exam-question/upload-image")
    public ResponseEntity<?> uploadExamQuestionImages(@RequestParam("questionImage") List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.FILE_REQUIRED.getValue(), MessageConstant.ExamQuestion.FILE_REQUIRED),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            // Đường dẫn thư mục tĩnh cho ảnh
            String imagePath = "images/";
            Path uploadImagePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", imagePath);

            if (!Files.exists(uploadImagePath)) {
                Files.createDirectories(uploadImagePath);
            }

            List<String> imageNames = new ArrayList<>();

            for (MultipartFile file : files) {
                String imageName = file.getOriginalFilename();
                Path imageFile = uploadImagePath.resolve(imageName);

                try (OutputStream osImage = Files.newOutputStream(imageFile)) {
                    osImage.write(file.getBytes());
                }
                imageNames.add(imageName);
            }

            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPLOAD_FILE_SUCCESS.getValue(), MessageConstant.ExamQuestion.UPLOAD_FILE_SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPLOAD_FILE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/admin/exam-question/upload-audio")
    public ResponseEntity<?> uploadExamQuestionAudios(@RequestParam("questionAudio") List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.FILE_REQUIRED.getValue(), MessageConstant.ExamQuestion.FILE_REQUIRED),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            // Đường dẫn thư mục tĩnh cho âm thanh
            String audioPath = "audios/";
            Path uploadAudioPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", audioPath);

            if (!Files.exists(uploadAudioPath)) {
                Files.createDirectories(uploadAudioPath);
            }

            List<String> audioNames = new ArrayList<>();

            for (MultipartFile file : files) {
                String audioName = file.getOriginalFilename();
                Path audioFile = uploadAudioPath.resolve(audioName);

                try (OutputStream osAudio = Files.newOutputStream(audioFile)) {
                    osAudio.write(file.getBytes());
                }
                audioNames.add(audioName);
            }

            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPLOAD_FILE_SUCCESS.getValue(), MessageConstant.ExamQuestion.UPLOAD_FILE_SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPLOAD_FILE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExamQuestionController {

    @Autowired
    private IExamQuestionService iExamQuestionService;

    @GetMapping("/admin/exam-question/get-all")
    public ResponseData<List<ExamQuestionResponse>> getAllExamQuestions() {
        List<ExamQuestionResponse> examQuestionList = iExamQuestionService.getAllExamQuestions().stream().map(
                ExamQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.ExamQuestion.GET_DATA_SUCCESS, examQuestionList);
    }

    @GetMapping("/admin/exam-question/get-by-id/{id}")
    public ResponseData<ExamQuestionResponse> getExamQuestionById(@PathVariable("id") @Min(1) Integer id) {
        ExamQuestionResponse examQuestion = ExamQuestionMapper.mapFromEntityToResponse(iExamQuestionService.getExamQuestionById(id));

        if (examQuestion != null) {
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.ExamQuestion.GET_DATA_SUCCESS, examQuestion);
        } else {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), MessageConstant.ExamQuestion.DATA_NOT_FOUND);
        }
    }

    @PostMapping("/admin/exam-question/create")
    public ResponseData<?> createExamQuestion(@ModelAttribute @Valid ExamQuestionRequest examQuestionRequest) {
        try {
            ExamQuestion createdExamQuestion = iExamQuestionService.createExamQuestion(examQuestionRequest);

            if(createdExamQuestion != null) {
                return new ResponseData<>(HttpStatus.CREATED.value(), MessageConstant.ExamQuestion.CREATE_SUCCESS);
            } else {
                return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.ExamQuestion.CREATE_FAILED);
            }
        } catch (IOException e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PutMapping("/admin/exam-question/update")
    public ResponseData<?> updateExamQuestion(@ModelAttribute @Valid ExamQuestionRequest examQuestionRequest) {
        try {
            ExamQuestion updatedExamQuestion = iExamQuestionService.updateExamQuestion(examQuestionRequest);

            if (updatedExamQuestion != null) {
                return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.ExamQuestion.UPDATE_SUCCESS);
            } else {
                return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.ExamQuestion.UPDATE_FAILED);
            }
        } catch (IOException e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @DeleteMapping("/admin/exam-question/delete/{id}")
    public ResponseData<MessageResponse> deleteExamQuestion(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iExamQuestionService.deleteExamQuestion(id);

        if(result) {
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.ExamQuestion.DELETE_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.ExamQuestion.DELETE_FAILED);
        }
    }

    @GetMapping("/user/exam-question/get-question-by-exam/{examId}")
    public ResponseData<List<ExamQuestionResponse>> getQuestionsByExamIdRoleUser(@PathVariable("examId") @Min(1) Integer examId) {
        List<ExamQuestionResponse> examQuestionList = iExamQuestionService.getExamQuestionsByExamId(examId).stream().map(
                ExamQuestionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!examQuestionList.isEmpty()) {
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.ExamQuestion.GET_DATA_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), MessageConstant.ExamQuestion.DATA_NOT_FOUND);
        }
    }

    @GetMapping("/admin/exam-question/get-question-by-exam/{examId}")
    public ResponseData<List<ExamQuestionResponse>> getQuestionsByExamIdRoleAdmin(@PathVariable("examId") @Min(1) Integer examId) {
        return getQuestionsByExamIdRoleUser(examId);
    }

    @DeleteMapping("/admin/exam-question/delete-question-by-exam/{examId}")
    public ResponseData<MessageResponse> deleteExamQuestionsByExamId(@PathVariable Integer examId) {
        boolean result = iExamQuestionService.deleteAllExamQuestionsByExamId(examId);

        if(result) {
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.ExamQuestion.DELETE_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.ExamQuestion.DELETE_FAILED);
        }

    }

    @PostMapping("/admin/exam-question/upload-excel")
    public ResponseData<?> uploadExamQuestionsFromExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("examId") Integer examId) {

        if (file == null || file.isEmpty()) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.ExamQuestion.FILE_REQUIRED);
        }

        try {
            boolean resulst = iExamQuestionService.uploadExamQuestionsFromExcel(file, examId);

            if(resulst) {
                return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.ExamQuestion.UPLOAD_FILE_SUCCESS);
            } else {
                return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.ExamQuestion.UPLOAD_FILE_FAILED);
            }
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/admin/exam-question/upload-image")
    public ResponseData<?> uploadExamQuestionImages(@RequestParam("questionImage") List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.ExamQuestion.FILE_REQUIRED);
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

            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.ExamQuestion.UPLOAD_FILE_SUCCESS);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PostMapping("/admin/exam-question/upload-audio")
    public ResponseData<?> uploadExamQuestionAudios(@RequestParam("questionAudio") List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.ExamQuestion.FILE_REQUIRED);
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

            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.ExamQuestion.UPLOAD_FILE_SUCCESS);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

}
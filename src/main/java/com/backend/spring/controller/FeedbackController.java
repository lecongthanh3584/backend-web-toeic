package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.FeedBackMapper;
import com.backend.spring.entity.Feedback;
import com.backend.spring.payload.request.FeedbackRequest;
import com.backend.spring.payload.response.FeedBackResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.Feedback.IFeedbackService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Validated
public class FeedbackController {

    @Autowired
    private IFeedbackService iFeedbackService;

    @GetMapping("/public/feedback/get-all")
    public ResponseEntity<?> getAllFeedback() {
        List<FeedBackResponse> feedbackList = iFeedbackService.getAllFeedback().stream().map(
                FeedBackMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Feedback.GET_DATA_SUCCESS ,feedbackList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/feedback/get-by-id/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable("id") @Min(1) Integer id) {
        FeedBackResponse feedback = FeedBackMapper.mapFromEntityToResponse(iFeedbackService.getFeedbackById(id));

        if (feedback != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Feedback.GET_DATA_SUCCESS, feedback),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Feedback.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/public/feedback/create")
    public ResponseEntity<?> createFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {

        Feedback createdFeedback = iFeedbackService.createFeedback(feedbackRequest);

        if(createdFeedback != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Feedback.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Feedback.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/feedback/update")
    public ResponseEntity<?> updateFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {
        Feedback updatedFeedback = iFeedbackService.updateFeedback(feedbackRequest);

        if (updatedFeedback != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Feedback.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Feedback.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/feedback/delete/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iFeedbackService.deleteFeedback(id);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.Feedback.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.Feedback.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/feedback/total")
    public ResponseEntity<?> countTotalFeedback() {
        long totalFeedback = iFeedbackService.countTotalFeedback();

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Feedback.GET_DATA_SUCCESS, totalFeedback),
                HttpStatus.OK);
    }

    @GetMapping("/admin/feedback/rating-percentages")
    public ResponseEntity<?> getFeedbackPercentagesByRating() {
        try {
            Map<Integer, Double> feedbackPercentagesByRating = iFeedbackService.calculatePercentageFeedbackByRating();

            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Feedback.GET_DATA_SUCCESS, feedbackPercentagesByRating),
                    HttpStatus.OK);
        } catch (Exception e) {
            Map<Integer, Double> response = new HashMap<>();
            response.put(-1, 0.0); // Đặt giá trị âm để biểu thị lỗi

            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Feedback.DATA_NOT_FOUND),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/public/feedback/five-star")
    public ResponseEntity<?> getFiveStarFeedbacks() {
        List<FeedBackResponse> fiveStarFeedbacks = iFeedbackService.getFiveStarFeedbacks().stream().map(
                FeedBackMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Feedback.GET_DATA_SUCCESS, fiveStarFeedbacks),
                HttpStatus.OK);
    }
}

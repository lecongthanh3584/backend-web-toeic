package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class FeedbackController {

    @Autowired
    private IFeedbackService iFeedbackService;

    @GetMapping("/public/feedback/get-all")
    public ResponseData<List<FeedBackResponse>> getAllFeedback() {
        List<FeedBackResponse> feedbackList = iFeedbackService.getAllFeedback().stream().map(
                FeedBackMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.Feedback.GET_DATA_SUCCESS, feedbackList);
    }

    @GetMapping("/admin/feedback/get-by-id/{id}")
    public ResponseData<FeedBackResponse> getFeedbackById(@PathVariable("id") @Min(1) Integer id) {
        FeedBackResponse feedback = FeedBackMapper.mapFromEntityToResponse(iFeedbackService.getFeedbackById(id));

        if (feedback != null) {
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.Feedback.GET_DATA_SUCCESS, feedback);
        } else {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), MessageConstant.Feedback.DATA_NOT_FOUND);
        }
    }

    @PostMapping("/public/feedback/create")
    public ResponseData<?> createFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {

        Feedback createdFeedback = iFeedbackService.createFeedback(feedbackRequest);

        if(createdFeedback != null) {
            return new ResponseData<>(HttpStatus.CREATED.value(), MessageConstant.Feedback.CREATE_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.Feedback.CREATE_FAILED);
        }
    }

    @PutMapping("/admin/feedback/update")
    public ResponseData<?> updateFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {
        Feedback updatedFeedback = iFeedbackService.updateFeedback(feedbackRequest);

        if (updatedFeedback != null) {
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.Feedback.UPDATE_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.Feedback.UPDATE_FAILED);
        }
    }

    @DeleteMapping("/admin/feedback/delete/{id}")
    public ResponseData<?> deleteFeedback(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iFeedbackService.deleteFeedback(id);

        if(result) {
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.Feedback.DELETE_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.Feedback.DELETE_FAILED);
        }
    }

    @GetMapping("/admin/feedback/total")
    public ResponseData<?> countTotalFeedback() {
        long totalFeedback = iFeedbackService.countTotalFeedback();

        return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.Feedback.GET_DATA_SUCCESS, totalFeedback);
    }

    @GetMapping("/admin/feedback/rating-percentages")
    public ResponseData<Map<Integer, Double>> getFeedbackPercentagesByRating() {
        try {
            Map<Integer, Double> feedbackPercentagesByRating = iFeedbackService.calculatePercentageFeedbackByRating();
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.Feedback.GET_DATA_SUCCESS, feedbackPercentagesByRating);
        } catch (Exception e) {
            Map<Integer, Double> response = new HashMap<>();
            response.put(-1, 0.0); // Đặt giá trị âm để biểu thị lỗi
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MessageConstant.Feedback.DATA_NOT_FOUND, response);
        }
    }

    @GetMapping("/public/feedback/five-star")
    public ResponseData<List<FeedBackResponse>> getFiveStarFeedbacks() {
        List<FeedBackResponse> fiveStarFeedbacks = iFeedbackService.getFiveStarFeedbacks().stream().map(
                FeedBackMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.Feedback.GET_DATA_SUCCESS, fiveStarFeedbacks);
    }
}

package com.backend.spring.service.Feedback;

import com.backend.spring.entity.Feedback;
import com.backend.spring.payload.request.FeedbackRequest;

import java.util.List;
import java.util.Map;

public interface IFeedbackService {
    List<Feedback> getAllFeedback();
    Feedback getFeedbackById(Integer id);
    Feedback createFeedback(FeedbackRequest feedbackRequest);
    Feedback updateFeedback(FeedbackRequest feedbackRequest);
    boolean deleteFeedback(Integer id);
    long countTotalFeedback();
    Map<Integer, Double> calculatePercentageFeedbackByRating();
    List<Feedback> getFiveStarFeedbacks();
}

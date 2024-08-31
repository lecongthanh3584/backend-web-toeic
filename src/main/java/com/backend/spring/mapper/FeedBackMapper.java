package com.backend.spring.mapper;

import com.backend.spring.entity.Feedback;
import com.backend.spring.payload.response.FeedBackResponse;

public class FeedBackMapper {
    public static FeedBackResponse mapFromEntityToResponse(Feedback feedback) {
        if(feedback == null) {
            return null;
        }

        return new FeedBackResponse(
            feedback.getFeedbackId(),
            feedback.getName(),
            feedback.getEmail(),
            feedback.getReview(),
            feedback.getRating(),
            feedback.getCreatedAt(),
            feedback.getUpdatedAt()
        );
    }
}

package com.backend.spring.services.Feedback;

import com.backend.spring.entities.Feedback;
import com.backend.spring.payload.request.FeedbackRequest;
import com.backend.spring.repositories.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FeedbackService implements IFeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Feedback getFeedbackById(Integer id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    @Override
    public Feedback createFeedback(FeedbackRequest feedbackRequest) {
        Feedback feedback = new Feedback();
        feedback.setName(feedbackRequest.getName());
        feedback.setEmail(feedbackRequest.getEmail());
        feedback.setReview(feedbackRequest.getReview());
        feedback.setRating(feedbackRequest.getRating());
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setUpdatedAt(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback updateFeedback(FeedbackRequest feedbackRequest) {
        Optional<Feedback> feedbackOptional = feedbackRepository.findById(feedbackRequest.getFeedBackId());
        if(feedbackOptional.isEmpty()) {
            return null;
        }

        Feedback existingFeedback = feedbackOptional.get();
        existingFeedback.setName(feedbackRequest.getName());
        existingFeedback.setEmail(feedbackRequest.getEmail());
        existingFeedback.setReview(feedbackRequest.getReview());
        existingFeedback.setRating(feedbackRequest.getRating());
        existingFeedback.setUpdatedAt(LocalDateTime.now());

        return feedbackRepository.save(existingFeedback);
    }

    @Override
    public boolean deleteFeedback(Integer id) {
        Optional<Feedback> feedbackOptional = feedbackRepository.findById(id);
        if(feedbackOptional.isEmpty()) {
            return false;
        }

        feedbackRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotalFeedback() {
        return feedbackRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Double> calculatePercentageFeedbackByRating() {
        List<Feedback> feedbackList = feedbackRepository.findAll();

        long totalFeedback = feedbackList.size();

        Map<Integer, Long> feedbackCountByRating = feedbackList.stream()
                .collect(Collectors.groupingBy(Feedback::getRating, Collectors.counting()));

        Map<Integer, Double> percentageFeedbackByRating = new HashMap<>();

        for (Map.Entry<Integer, Long> entry : feedbackCountByRating.entrySet()) {
            int rating = entry.getKey();
            long count = entry.getValue();

            // Tính toán phần trăm và thêm vào map
            double percentage = (count * 100.0) / totalFeedback;
            percentageFeedbackByRating.put(rating, percentage);
        }

        return percentageFeedbackByRating;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getFiveStarFeedbacks() {
        List<Feedback> feedbackList = feedbackRepository.findAll();

        return feedbackList.stream()
                .filter(feedback -> feedback.getRating() == 5)
                .collect(Collectors.toList());
    }

}

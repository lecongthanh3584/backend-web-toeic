package com.backend.spring.service.UserGoal;

import com.backend.spring.entity.User;
import com.backend.spring.entity.UserGoal;
import com.backend.spring.payload.request.UserGoalRequest;
import com.backend.spring.repository.UserGoalRepository;
import com.backend.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserGoalService implements IUserGoalService {

    @Autowired
    private UserGoalRepository userGoalRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserGoal createUserGoal(UserGoalRequest userGoalRequest) {
        Optional<User> userOptional = userRepository.findById(userGoalRequest.getUserId());

        if (userOptional.isPresent()) {
            UserGoal userGoal = new UserGoal();

            userGoal.setUser(userOptional.get());
            userGoal.setGoalScore(userGoalRequest.getGoalScore());
            userGoal.setCreatedAt(LocalDateTime.now());
            userGoal.setUpdatedAt(LocalDateTime.now());

            return userGoalRepository.save(userGoal);
        }

        return null;
    }

    @Override
    public UserGoal updateUserGoalByUserId(UserGoalRequest userGoalRequest) {
        Optional<User> userOptional = userRepository.findById(userGoalRequest.getUserId());
        Optional<UserGoal> userGoalOptional = userGoalRepository.findById(userGoalRequest.getUserGoalId());

        if(userOptional.isPresent() && userGoalOptional.isPresent()) {
            UserGoal userGoalUpdate = userGoalOptional.get();

            userGoalUpdate.setGoalScore(userGoalRequest.getGoalScore());
            userGoalUpdate.setUser(userOptional.get());
            userGoalUpdate.setUpdatedAt(LocalDateTime.now());

            return userGoalRepository.save(userGoalUpdate);
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public UserGoal getUserGoalById(Integer userGoalId) {
        return userGoalRepository.findById(userGoalId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGoal> getAllUserGoals() {
        return userGoalRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserGoal getUserGoalByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            return userGoalRepository.findByUser(userOptional.get()).orElse(null);
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserGoalWithUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            Optional<UserGoal> userGoal = userGoalRepository.findByUser(userOptional.get());

            return userGoal.isPresent(); // Tìm thấy (true)
        }

        return false; //Không tìm thấy bài thi hoặc người dùng
    }
}

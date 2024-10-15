package com.backend.spring.services.UserGoal;

import com.backend.spring.entities.User;
import com.backend.spring.entities.UserGoal;
import com.backend.spring.payload.request.UserGoalRequest;
import com.backend.spring.repositories.UserGoalRepository;
import com.backend.spring.repositories.UserRepository;
import com.backend.spring.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserGoalService implements IUserGoalService {

    private final UserGoalRepository userGoalRepository;

    private final UserRepository userRepository;

    @Override
    public UserGoal createUserGoal(UserGoalRequest userGoalRequest) {
        User userLogin = UserUtil.getDataUserLogin();

        if (userLogin != null) {
            UserGoal userGoal = new UserGoal();

            userGoal.setUser(userLogin);
            userGoal.setGoalScore(userGoalRequest.getGoalScore());

            return userGoalRepository.save(userGoal);
        }

        return null;
    }

    @Override
    public UserGoal updateUserGoalByUserId(UserGoalRequest userGoalRequest) {
        User userLogin = UserUtil.getDataUserLogin();
        Optional<UserGoal> userGoalOptional = userGoalRepository.findById(userGoalRequest.getUserGoalId());

        if(userLogin != null && userGoalOptional.isPresent()) {
            UserGoal userGoalUpdate = userGoalOptional.get();

            userGoalUpdate.setGoalScore(userGoalRequest.getGoalScore());
            userGoalUpdate.setUser(userLogin);

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

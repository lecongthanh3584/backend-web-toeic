package com.backend.spring.service.User;

import com.backend.spring.enums.ERole;
import com.backend.spring.enums.EStatus;
import com.backend.spring.entity.User;
import com.backend.spring.payload.request.ChangePasswordRequest;
import com.backend.spring.payload.request.ProfileRequest;
import com.backend.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findByRoleName(String roleName) {
        return userRepository.findByRoles(roleName);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countLearner() {
        return userRepository.countByRoles(ERole.LEARNER.getValue());
    }

    @Override
    public User updateStatus(Integer userId, Integer newStatus) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            return null;
        }

        User userUpdate = userOptional.get();

        if(newStatus.equals(EStatus.DISABLE.getValue())) {
            userUpdate.setStatus(newStatus);
        } else if(newStatus.equals(EStatus.ENABLE.getValue())) {
            userUpdate.setStatus(newStatus);
        } else {
            throw new IllegalArgumentException("Invalid new status value!");
        }

        userUpdate.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(userUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getUserIdByUserName(String userName) {
        Optional<User> userOptional = userRepository.findByUsername(userName);
        if(userOptional.isEmpty()) {
            return 0;
        }

        return userOptional.get().getUserId();

    }

    @Override
    public User updateProfile(ProfileRequest profileRequest) {
        Optional<User> userOptional = userRepository.findById(profileRequest.getUserId());
        if(userOptional.isEmpty()) {
            return null;
        }

        User userUpdate = userOptional.get();
        userUpdate.setFullName(profileRequest.getFullName());
        userUpdate.setAddress(profileRequest.getAddress());
        userUpdate.setGender(profileRequest.getGender());
        userUpdate.setPhoneNumber(profileRequest.getPhoneNumber());
        userUpdate.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(userUpdate);
    }

    @Override
    public User updatePassword(ChangePasswordRequest changePasswordRequest) {
        Optional<User> userOptional = userRepository.findById(changePasswordRequest.getUserId());
        if(userOptional.isEmpty()) {
            return null;
        }

        User userUpdate = userOptional.get();

        if(!passwordEncoder.matches(changePasswordRequest.getOldPassWord(), userUpdate.getPassword())) {
            return null;
        }

        userUpdate.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassWord()));
        userUpdate.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(userUpdate);
    }

}

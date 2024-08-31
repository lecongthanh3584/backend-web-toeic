package com.backend.spring.service.User;

import com.backend.spring.entity.User;
import com.backend.spring.payload.request.ChangePasswordRequest;
import com.backend.spring.payload.request.ProfileRequest;

import java.util.List;

public interface IUserService {
    List<User> findByRoleName(String roleName);
    User getUserById(Integer userId);
    Long countLearner();
    User updateStatus(Integer userId, Integer newStatus);
    Integer getUserIdByUserName(String userName);
    User updateProfile(ProfileRequest profileRequest);
    User updatePassword(ChangePasswordRequest changePasswordRequest);
}

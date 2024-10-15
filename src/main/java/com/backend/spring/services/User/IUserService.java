package com.backend.spring.services.User;

import com.backend.spring.entities.User;
import com.backend.spring.exception.AlreadyExistsException;
import com.backend.spring.exception.NotFoundException;
import com.backend.spring.payload.request.*;
import com.backend.spring.payload.response.RefreshTokenResponse;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    List<User> findByRoleName(String roleName);
    User getUserById(Integer userId);
    Long countLearner();
    User updateStatus(Integer userId, Integer newStatus);
    Integer getUserIdByUserName(String userName);
    User updateProfile(ProfileRequest profileRequest);
    User updateImageProfile(ProfileImageRequest profileImageRequest) throws IOException, NotFoundException;
    User updatePassword(ChangePasswordRequest changePasswordRequest);
}

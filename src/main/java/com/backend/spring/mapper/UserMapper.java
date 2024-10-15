package com.backend.spring.mapper;

import com.backend.spring.entities.User;
import com.backend.spring.payload.response.UserResponse;

public class UserMapper {
    public static UserResponse mapFromEntityToResponse(User user) {
        if(user == null) {
            return null;
        }

        return new UserResponse (
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName(),
            user.getAddress(),
            user.getPhoneNumber(),
            user.getProvider(),
            user.getImage(),
            user.getStatus(),
            user.getIsActive(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}

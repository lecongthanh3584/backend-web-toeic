package com.backend.spring.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserResponse {
    private Integer userId;

    private String username;

    private String email;

    private String fullName;

    private String address;

    private String phoneNumber;

    private Integer provider;

    private String image;

    private Integer status;

    private Integer isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;
}

package com.backend.spring.payload.response;

import com.backend.spring.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    private Integer gender;

    private String image;

    private Integer status;

    private Integer isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;
}

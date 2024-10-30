package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class LoginRequest {
    @NotBlank(message = "email không được để trống!")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống!")
    @Size(min = 6, max = 40, message = "Mật khẩu tối thiểu 6 ký tự và tối đa 40 ký tự!")
    private String password;
}

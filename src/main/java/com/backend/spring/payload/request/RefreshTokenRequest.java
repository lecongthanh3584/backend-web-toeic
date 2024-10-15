package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh Token không được trống!")
    private String refreshToken;
}

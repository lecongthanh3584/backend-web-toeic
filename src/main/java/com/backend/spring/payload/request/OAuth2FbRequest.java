package com.backend.spring.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2FbRequest {

    @NotBlank(message = "FullName không được trống.")
    private String fullName;

    @NotBlank(message = "Email không được trống.")
    @Email(message = "Email không đúng định dạng.")
    private String email;

    @NotBlank(message = "picture không được trống.")
    private String image;
}


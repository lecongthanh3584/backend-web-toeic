package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2GoogleRequest {

    @NotBlank(message = "Token oauth2 không được trống.")
    private String accessTokenOAuth2;
}

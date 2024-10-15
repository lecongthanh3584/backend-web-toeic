package com.backend.spring.payload.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RefreshTokenResponse {
    private String accessToken;

    private String refreshToken;

    private Long accessTokenExpirationTime;

}

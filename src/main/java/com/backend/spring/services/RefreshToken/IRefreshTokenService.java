package com.backend.spring.services.RefreshToken;

import com.backend.spring.payload.request.RefreshTokenRequest;
import com.backend.spring.payload.response.RefreshTokenResponse;

public interface IRefreshTokenService {
    RefreshTokenResponse getNewAccessToken(RefreshTokenRequest request) throws Exception;
    String generateRefreshToken(Integer userId);
}

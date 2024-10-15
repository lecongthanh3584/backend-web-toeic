package com.backend.spring.services.RefreshToken;

import java.util.Optional;
import java.util.UUID;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.entities.User;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.exception.NotFoundException;
import com.backend.spring.exception.RefreshTokenException;
import com.backend.spring.payload.request.RefreshTokenRequest;
import com.backend.spring.payload.response.RefreshTokenResponse;
import com.backend.spring.repositories.UserRepository;
import com.backend.spring.security.jwt.JwtUtil;
import com.backend.spring.services.Redis.IRedisService;
import com.backend.spring.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {

    @Value("${security.jwt.rt-expiration-ms}")
    private Long RT_EXPIRATION_MS;

    @Value("${security.jwt.at-expiration-ms}")
    private Long AT_EXPIRATION_MS;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final IRedisService iRedisService;

    @Override
    public RefreshTokenResponse getNewAccessToken(RefreshTokenRequest request) throws Exception {
        User userLogin = UserUtil.getDataUserLogin();

        String key = MessageConstant.Redis.REFRESH_TOKEN + ":" + userLogin.getUserId();
        String refreshTokenFromRedis = (String) iRedisService.getDataCache(key);

        if(refreshTokenFromRedis == null) {
            throw new RefreshTokenException(EStatusCode.REFRESH_TOKEN_EXPIRED.getValue(), MessageConstant.Auth.REFRESH_TOKEN_EXPIRED);
        } else if (!refreshTokenFromRedis.equals(request.getRefreshToken())) {
            throw new RefreshTokenException(EStatusCode.INVALID_REFRESH_TOKEN.getValue(), MessageConstant.Auth.INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtUtil.generateAccessToken(userLogin);
        String newRefreshToken = UUID.randomUUID().toString();

        //Tao moi refreshToken, luu no voi thoi gian het han cua refreshToken cu
        iRedisService.setDataCache(key, newRefreshToken, iRedisService.getExpireTime(key));

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken).refreshToken(newRefreshToken)
                .accessTokenExpirationTime(AT_EXPIRATION_MS).build();
    }

    public String generateRefreshToken(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new RuntimeException("Error. User login not found!");
        }

        String key = MessageConstant.Redis.REFRESH_TOKEN + ":" + userId;
        String refreshToken = UUID.randomUUID().toString();

        iRedisService.setDataCache(key, refreshToken, RT_EXPIRATION_MS);

        return refreshToken;
    }
}

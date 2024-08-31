package com.backend.spring.security.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.backend.spring.entity.User;
import com.backend.spring.exception.NotFoundException;
import com.backend.spring.exception.TokenRefreshException;
import com.backend.spring.entity.RefreshToken;
import com.backend.spring.repository.RefreshTokenRepository;
import com.backend.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

    @Value("${RT_EXPIRATION_MS}")
    private Long RT_EXPIRATION_MS;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken generateRefreshToken(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new RuntimeException("Error. User login not found!");
        }

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userOptional.get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(RT_EXPIRATION_MS));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Integer userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    public long getRefreshTokenDurationMs() {
        return this.RT_EXPIRATION_MS;
    }
}

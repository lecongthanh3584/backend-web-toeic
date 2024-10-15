package com.backend.spring.repositories;

import com.backend.spring.entities.AuthToken;
import com.backend.spring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {
    Optional<AuthToken> findByToken(String token);
    Optional<AuthToken> findByTokenAndUser(String token, User user);
}

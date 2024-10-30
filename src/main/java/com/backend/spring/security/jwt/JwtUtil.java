package com.backend.spring.security.jwt;

import java.security.Key;
import java.util.Date;

import com.backend.spring.entities.User;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
@Slf4j
public class JwtUtil {
  @Value("${security.jwt.secret-key}")
  private String SECRET_KEY;

  @Value("${security.jwt.at-expiration-ms}")
  private final long AT_EXPIRATION_MS = 86400000;

  Date date = new Date();
  Date expireTime = new Date(date.getTime() + AT_EXPIRATION_MS);

  public String generateAccessToken(User user) {
    return Jwts.builder()
            .setSubject(String.format("%s, %s", user.getUserId(), user.getEmail()))
            .setIssuedAt(date)
            .setExpiration(expireTime)
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public boolean validateAccessToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException ex) {
      log.error("JWT expired", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      log.error("Token is null, empty or only whitespace", ex.getMessage());
    } catch (MalformedJwtException ex) {
      log.error("JWT is invalid", ex);
    } catch (UnsupportedJwtException ex) {
      log.error("JWT is not supported", ex);
    } catch (SignatureException ex) {
      log.error("Signature validation failed");
    }
    return false;
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String getSubject(String token) {
    return parseClaims(token).getSubject();
  }

  public Claims parseClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getSignInKey()).build()
            .parseClaimsJws(token).getBody();
  }

  public long getAccessTokenDurationMs() {
    return this.AT_EXPIRATION_MS;
  }
}

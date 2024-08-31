package com.backend.spring.security.jwt;

import java.util.Date;

import com.backend.spring.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
@Slf4j
public class JwtUtil {
  @Value("${JWT_SECRET_KEY}")
  private String SECRET_KEY;

//  @Value("${AT_EXPIRATION_MS}")
  private final long AT_EXPIRATION_MS = 86400000;

  Date date = new Date();
  Date expireTime = new Date(date.getTime() + AT_EXPIRATION_MS);

  public String generateAccessToken(User user) {
    return Jwts.builder()
            .setSubject(String.format("%s, %s", user.getUserId(), user.getUsername()))
            .setIssuedAt(date)
            .setExpiration(expireTime)
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
  }

  public boolean validateAccessToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
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

  public String getSubject(String token) {
    return parseClaims(token).getSubject();
  }

  public Claims parseClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()
            .parseClaimsJws(token).getBody();
  }

  public long getAccessTokenDurationMs() {
    return this.AT_EXPIRATION_MS;
  }
}

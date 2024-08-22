package com.ansbeno.books_service.security.config;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.ansbeno.books_service.SecurityProperties;
import com.ansbeno.books_service.domain.user.UserEntity;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
      private final SecurityProperties properties;

      private SecretKey accessKey;

      private SecretKey refreshKey;

      @PostConstruct
      public void init() {
            accessKey = Keys.hmacShaKeyFor(properties.accessTokenSecretKey().getBytes());
            refreshKey = Keys.hmacShaKeyFor(properties.refreshTokenSecretKey().getBytes());
      }

      public String generateAccessToken(UserEntity user, Map<String, Object> extraClaims) {
            Date issueAt = new Date(System.currentTimeMillis());
            Date expiration = new Date(issueAt.getTime() + (properties.accessTokenExpirationMinutes() * 60 * 1000));
            return Jwts.builder()
                        .claims(extraClaims)
                        .subject(user.getUsername())
                        .issuedAt(issueAt)
                        .expiration(expiration)
                        .signWith(accessKey, SIG.HS256)
                        .compact();
      }

      public String extractUsernameFromAccessToken(String jwt) {
            return Jwts.parser()
                        .verifyWith(accessKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload()
                        .getSubject();
      }

      public String generateRefreshToken(UserEntity user, Map<String, Object> extraClaims) {
            Date issueAt = new Date(System.currentTimeMillis());
            Date expiration = new Date(issueAt.getTime() + (properties.refreshTokenExpirationMinutes() * 60 * 1000));
            return Jwts.builder()
                        .claims(extraClaims)
                        .subject(user.getUsername())
                        .issuedAt(issueAt)
                        .expiration(expiration)
                        .signWith(refreshKey, SIG.HS256)
                        .compact();
      }

      public String extractUsernameFromRefreshToken(String jwt) {
            return Jwts.parser()
                        .verifyWith(refreshKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload()
                        .getSubject();
      }

}

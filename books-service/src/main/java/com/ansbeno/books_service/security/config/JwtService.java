package com.ansbeno.books_service.security.config;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ansbeno.books_service.domain.user.UserEntity;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

      @Value("${jwtsecurity.jwt-expiration-minutes}")
      private Long expirationMinutes;

      @Value("${jwtsecurity.jwt-secret-key}")
      private String secretKey;

      private SecretKey key;

      @PostConstruct
      public void init() {
            key = Keys.hmacShaKeyFor(secretKey.getBytes());
      }

      public String generateToken(UserEntity user, Map<String, Object> extraClaims) {
            Date issueAt = new Date(System.currentTimeMillis());
            Date expiration = new Date(issueAt.getTime() + (expirationMinutes * 60 * 1000));
            return Jwts.builder()
                        .claims(extraClaims)
                        .subject(user.getUsername())
                        .issuedAt(issueAt)
                        .expiration(expiration)
                        .signWith(key, SIG.HS256)
                        .compact();
      }

      public String extractUsername(String jwt) {
            return Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload()
                        .getSubject();
      }

}

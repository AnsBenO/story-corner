package com.ansbeno.books_service.security;

import java.util.Map;
import java.util.HashMap;

import com.ansbeno.books_service.domain.user.UserEntity;
import com.ansbeno.books_service.security.config.JwtService;

public class JwtTokenUtil {

      public static String generateMockJwtToken(JwtService jwtService, UserEntity user) {
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("name", user.getUsername());
            extraClaims.put("role", user.getRole().name());

            return jwtService.generateToken(user, extraClaims);
      }
}

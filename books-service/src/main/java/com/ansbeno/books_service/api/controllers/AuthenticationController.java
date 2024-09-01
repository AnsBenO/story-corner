package com.ansbeno.books_service.api.controllers;

import com.ansbeno.books_service.SecurityProperties;
import com.ansbeno.books_service.security.authentication.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
      private final AuthenticationService authenticationService;
      private final SecurityProperties securityProperties;

      @PostMapping("/register")
      ResponseEntity<AuthenticationResponseDto> register(@Valid @RequestBody RegisterUserDto request,
                  HttpServletResponse response) {
            AuthenticationDto auth = authenticationService.register(request);
            Cookie refreshTokenCookie = createRefreshTokenCookie(auth.refreshToken());
            response.addCookie(refreshTokenCookie);
            AuthenticationResponseDto authResponse = new AuthenticationResponseDto(auth.accessToken(), auth.user());
            return ResponseEntity.ok(authResponse);
      }

      @PostMapping("/login")
      ResponseEntity<AuthenticationResponseDto> authenticate(@Valid @RequestBody AuthenticationRequestDto request,
                  HttpServletResponse response) {
            AuthenticationDto auth = authenticationService.login(request);
            Cookie refreshTokenCookie = createRefreshTokenCookie(auth.refreshToken());
            response.addCookie(refreshTokenCookie);
            AuthenticationResponseDto authResponse = new AuthenticationResponseDto(auth.accessToken(), auth.user());
            return ResponseEntity.ok(authResponse);
      }

      @GetMapping("/user")
      ResponseEntity<CurrentUserResponseDto> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
            String token = authHeader.split(" ")[1];
            return ResponseEntity.ok(authenticationService.getCurrentUser(token));
      }

      @PostMapping("/refresh-token")
      ResponseEntity<AuthenticationResponseDto> refreshToken(
                  @CookieValue(value = "refreshToken", required = true) String refreshToken,
                  HttpServletResponse response) {
            AuthenticationDto auth = authenticationService.refreshToken(refreshToken);
            AuthenticationResponseDto authResponse = new AuthenticationResponseDto(auth.accessToken(), auth.user());
            return ResponseEntity.ok(authResponse);
      }

      @PostMapping("/logout")
      public ResponseEntity<Map<String, String>> logout(
                  @CookieValue("refreshToken") String refreshToken,
                  @RequestHeader("Authorization") String accessTokenHeader,
                  HttpServletResponse response) {

            String accessToken = accessTokenHeader.startsWith("Bearer ")
                        ? accessTokenHeader.split(" ")[1]
                        : accessTokenHeader;

            // Perform the logout operation
            authenticationService.logout(refreshToken, accessToken);

            // Remove the refreshToken cookie by setting Max-Age to 0
            Cookie deleteCookie = createRefreshTokenCookie(null);
            deleteCookie.setMaxAge(0);

            Map<String, String> logoutResponse = new HashMap<>();
            logoutResponse.put("message", "Successfully logged out");
            response.addCookie(deleteCookie);
            return ResponseEntity.ok()
                        .body(logoutResponse);
      }

      private Cookie createRefreshTokenCookie(String refreshToken) {
            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/api/auth");
            cookie.setDomain("localhost");
            if (refreshToken != null) {
                  cookie.setMaxAge(securityProperties.refreshTokenExpirationMinutes() * 60);
            } else {
                  cookie.setMaxAge(0);
            }
            log.info("Updated cookie {}", cookie.toString());
            return cookie;
      }
}

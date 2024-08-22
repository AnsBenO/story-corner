package com.ansbeno.books_service.api.controllers;

import com.ansbeno.books_service.security.authentication.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
      private final AuthenticationService authenticationService;

      @PostMapping("/register")
      ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterUserDto request) {
            return ResponseEntity.ok(authenticationService.register(request));
      }

      @PostMapping("/login")
      ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
            return ResponseEntity.ok(authenticationService.login(request));
      }

      @GetMapping("/user")
      ResponseEntity<CurrentUserResponseDto> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
            String token = authHeader.split(" ")[1];
            return ResponseEntity.ok(authenticationService.getCurrentUser(token));
      }

      @PostMapping("/refresh-token")
      ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody String refreshToken) {
            return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
      }
}

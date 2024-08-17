package com.ansbeno.books_service.api.controllers;

import com.ansbeno.books_service.security.authentication.AuthenticationRequest;
import com.ansbeno.books_service.security.authentication.AuthenticationResponse;
import com.ansbeno.books_service.security.authentication.AuthenticationService;
import com.ansbeno.books_service.security.authentication.RegisterUserDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

package com.ansbeno.books_service.security.authentication;

import java.util.HashMap;
import java.util.Map;

import com.ansbeno.books_service.security.exceptions.UserRegistrationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ansbeno.books_service.domain.user.Role;
import com.ansbeno.books_service.domain.user.UserEntity;
import com.ansbeno.books_service.domain.user.UserRepository;
import com.ansbeno.books_service.security.config.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
      private final AuthenticationManager authenticationManager;
      private final UserRepository userRepository;
      private final JwtService jwtService;
      private final BCryptPasswordEncoder encoder;

      public AuthenticationResponse login(AuthenticationRequest request) {

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password());

            authenticationManager.authenticate(token);

            UserEntity user = userRepository.findByUsername(request.username()).orElseThrow(
                        () -> new UsernameNotFoundException("User " + request.username() + " not found"));
            String jwt = jwtService.generateToken(user, generateExtraClaims(user));

            return new AuthenticationResponse(jwt);
      }

      private Map<String, Object> generateExtraClaims(UserEntity user) {
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("name", user.getUsername());
            extraClaims.put("role", user.getRole().name());
            return extraClaims;

      }

      public AuthenticationResponse register(RegisterUserDto request) {
            if (userRepository.existsByUsername(request.username())) {
                  throw new UserRegistrationException("Username '" + request.username() + "' is already taken.");
            }
            UserEntity user = UserEntity.builder()
                        .username(request.username())
                        .email(request.email())
                        .phone(request.phone())
                        .password(encoder.encode(request.password()))
                        .country(request.country())
                        .role(Role.CUSTOMER)
                        .build();
            userRepository.save(user);
            String token = jwtService.generateToken(user, generateExtraClaims(user));
            return new AuthenticationResponse(token);

      }

      public CurrentUserResponseDto getCurrentUser(String token) {
            String username = jwtService.extractUsername(token);
            UserEntity user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
            return new CurrentUserResponseDto(
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone());

      }
}

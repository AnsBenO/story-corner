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
            String accessToken = jwtService.generateAccessToken(user, generateExtraClaims(user));
            String refreshToken = jwtService.generateRefreshToken(user, generateExtraClaims(user));
            CurrentUserResponseDto currentUser = new CurrentUserResponseDto(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone());
            return new AuthenticationResponse(accessToken, refreshToken, currentUser);
      }

      private Map<String, Object> generateExtraClaims(UserEntity user) {
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("name", user.getUsername());
            extraClaims.put("role", user.getRole().name());
            return extraClaims;

      }

      public AuthenticationResponse register(RegisterUserDto request) {

            StringBuilder errorMessage = new StringBuilder();
            if (userRepository.existsByUsername(request.username())) {
                  errorMessage.append("Username '").append("' is already taken.");
            }

            // Check if the email is already taken
            if (userRepository.existsByEmail(request.email())) {
                  if (errorMessage.length() > 0) {
                        errorMessage.append("|");
                  }
                  errorMessage.append("Email '").append("' is already taken.");
            }

            // If there are any errors, throw an exception with the accumulated message
            if (errorMessage.length() > 0) {
                  throw new UserRegistrationException(errorMessage.toString());
            }
            UserEntity user = UserEntity.builder()
                        .username(request.username())
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .email(request.email())
                        .phone(request.phone())
                        .password(encoder.encode(request.password()))
                        .country(request.country())
                        .role(Role.CUSTOMER)
                        .build();
            userRepository.save(user);
            String accessToken = jwtService.generateAccessToken(user, generateExtraClaims(user));
            String refreshToken = jwtService.generateRefreshToken(user, generateExtraClaims(user));
            CurrentUserResponseDto currentUser = new CurrentUserResponseDto(user.getFirstName(), user.getLastName(),
                        user.getUsername(), user.getEmail(), user.getPhone());
            return new AuthenticationResponse(accessToken, refreshToken, currentUser);

      }

      public CurrentUserResponseDto getCurrentUser(String token) {
            String username = jwtService.extractUsernameFromAccessToken(token);
            UserEntity user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
            return new CurrentUserResponseDto(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone());

      }

      public AuthenticationResponse refreshToken(String refreshToken) {
            String username = jwtService.extractUsernameFromRefreshToken(refreshToken);
            UserEntity user = userRepository.findByUsername(username).orElseThrow(
                        () -> new UsernameNotFoundException("User " + username + " not found"));
            String newAccessToken = jwtService.generateAccessToken(user, generateExtraClaims(user));
            CurrentUserResponseDto currentUser = new CurrentUserResponseDto(user.getFirstName(), user.getLastName(),
                        user.getUsername(), user.getEmail(), user.getPhone());
            return new AuthenticationResponse(newAccessToken, refreshToken, currentUser);

      }
}

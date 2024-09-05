package com.ansbeno.books_service.security.authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.ansbeno.books_service.security.exceptions.InvalidTokenException;
import com.ansbeno.books_service.security.exceptions.TokenExpiredException;
import com.ansbeno.books_service.security.exceptions.TokenNotFoundException;
import com.ansbeno.books_service.security.exceptions.TokenRevokedException;
import com.ansbeno.books_service.security.exceptions.UserRegistrationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ansbeno.books_service.domain.user.Role;
import com.ansbeno.books_service.domain.user.TokenEntity;
import com.ansbeno.books_service.domain.user.TokenRepository;
import com.ansbeno.books_service.domain.user.TokenType;
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
      private final TokenRepository tokenRepository;

      public AuthenticationDto login(AuthenticationRequestDto request) {

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password());

            authenticationManager.authenticate(token);

            UserEntity user = userRepository.findByUsername(request.username()).orElseThrow(
                        () -> new UsernameNotFoundException("User " + request.username() + " not found"));
            // generate tokens
            String accessToken = jwtService.generateAccessToken(user, generateExtraClaims(user));
            String refreshToken = jwtService.generateRefreshToken(user, generateExtraClaims(user));

            // save tokens
            saveToTokenRepository(refreshToken, user, TokenType.REFRESH_TOKEN);
            saveToTokenRepository(accessToken, user, TokenType.ACCESS_TOKEN);

            CurrentUserResponseDto currentUser = new CurrentUserResponseDto(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone());
            return new AuthenticationDto(accessToken, refreshToken, currentUser);
      }

      private Map<String, Object> generateExtraClaims(UserEntity user) {
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("name", user.getUsername());
            extraClaims.put("role", user.getRole().name());
            extraClaims.put("jti", UUID.randomUUID().toString());
            return extraClaims;

      }

      public AuthenticationDto register(RegisterUserDto request) {

            StringBuilder errorMessage = new StringBuilder();
            if (userRepository.existsByUsername(request.username())) {
                  errorMessage.append("Username is already taken.");
            }

            // Check if the email is already taken
            if (userRepository.existsByEmail(request.email())) {
                  if (errorMessage.length() > 0) {
                        errorMessage.append("/n");
                  }
                  errorMessage.append("Email is already taken.");
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
            // save tokens
            saveToTokenRepository(refreshToken, user, TokenType.REFRESH_TOKEN);
            saveToTokenRepository(accessToken, user, TokenType.ACCESS_TOKEN);
            CurrentUserResponseDto currentUser = new CurrentUserResponseDto(user.getFirstName(), user.getLastName(),
                        user.getUsername(), user.getEmail(), user.getPhone());
            return new AuthenticationDto(accessToken, refreshToken, currentUser);

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

      public AuthenticationDto refreshToken(String refreshToken) {
            // Retrieve the token entity and validate its state
            TokenEntity tokenEntity = tokenRepository.findByToken(refreshToken)
                        .orElseThrow(() -> new TokenNotFoundException("Invalid refresh token"));

            // Check if the token is revoked
            if (tokenRepository.existsByTokenAndRevoked(refreshToken, true)) {
                  throw new TokenRevokedException("Refresh token has been revoked");
            }

            // Check if the token is expired
            if (jwtService.isTokenExpired(refreshToken, TokenType.REFRESH_TOKEN)) {
                  throw new TokenExpiredException("Refresh token has expired");
            }

            // Update the usage count and possibly revoke the token if necessary
            tokenEntity.setUsageCount(tokenEntity.getUsageCount() + 1);
            if (tokenEntity.getUsageCount() >= 3) {
                  tokenEntity.setRevoked(true);
            }
            tokenRepository.save(tokenEntity);

            // Extract username and find the user
            String username = jwtService.extractUsernameFromRefreshToken(refreshToken);
            UserEntity user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

            // Generate a new access token
            String newAccessToken = jwtService.generateAccessToken(user, generateExtraClaims(user));

            // Save the new access token
            saveToTokenRepository(newAccessToken, user, TokenType.ACCESS_TOKEN);

            // Prepare the response DTO
            CurrentUserResponseDto currentUser = new CurrentUserResponseDto(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone());

            return new AuthenticationDto(newAccessToken, refreshToken, currentUser);
      }

      public void logout(String refreshToken, String accessToken) {
            TokenEntity refreshTokenEntity = tokenRepository.findByToken(refreshToken)
                        .orElseThrow(() -> new TokenNotFoundException("Invalid refresh token"));

            TokenEntity accessTokenEntity = tokenRepository.findByToken(accessToken)
                        .orElseThrow(() -> new TokenNotFoundException("Invalid access token"));

            // Ensure both tokens belong to the same user
            if (!refreshTokenEntity.getUser().equals(accessTokenEntity.getUser())) {
                  throw new InvalidTokenException("Tokens do not belong to the same user");
            }

            refreshTokenEntity.setRevoked(true);
            accessTokenEntity.setRevoked(true);

            tokenRepository.save(refreshTokenEntity);
            tokenRepository.save(accessTokenEntity);
      }

      private void saveToTokenRepository(String token, UserEntity user, TokenType type) {
            TokenEntity tokenToSave = TokenEntity.builder()
                        .token(token)
                        .user(user)
                        .tokenType(type).build();
            tokenRepository.save(tokenToSave);
      }
}

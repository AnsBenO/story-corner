package com.ansbeno.books_service.security.authentication;

public record AuthenticationResponse(
            String accessToken,
            CurrentUserResponseDto user) {
}

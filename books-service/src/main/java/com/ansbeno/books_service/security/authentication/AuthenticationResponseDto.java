package com.ansbeno.books_service.security.authentication;

public record AuthenticationResponseDto(
            String accessToken,
            CurrentUserResponseDto user) {
}

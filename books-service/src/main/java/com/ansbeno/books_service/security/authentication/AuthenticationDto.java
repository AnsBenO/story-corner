package com.ansbeno.books_service.security.authentication;

public record AuthenticationDto(
            String accessToken,
            String refreshToken,
            CurrentUserResponseDto user) {

}

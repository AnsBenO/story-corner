package com.ansbeno.books_service.security.authentication;

public record AuthenticationResponse(
            String accessToken,
            String refreshToken,
            CurrentUserResponseDto user
            ) {

}

package com.ansbeno.books_service.security.authentication;

public record AuthenticationResponse(
            String jwt,
            CurrentUserResponseDto user
            ) {

}

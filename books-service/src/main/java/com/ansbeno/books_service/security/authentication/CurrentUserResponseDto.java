package com.ansbeno.books_service.security.authentication;

public record CurrentUserResponseDto(
        String firstName,
        String lastName,
        String username,
        String email,
        String phone) {
}

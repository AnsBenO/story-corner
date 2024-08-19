package com.ansbeno.books_service.security.authentication;

public record CurrentUserResponseDto(
              String username,
              String email,
              String phone) {
}

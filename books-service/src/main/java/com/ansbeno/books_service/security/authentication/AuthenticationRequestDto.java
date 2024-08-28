package com.ansbeno.books_service.security.authentication;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequestDto(
                @NotBlank(message = "Username is required") String username,

                @NotBlank(message = "Password is required") String password) {

}

package com.ansbeno.books_service.security.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserDto(
                @NotBlank(message = "Username is required") @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters") String username,

                @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String password,

                @NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email,

                @NotBlank(message = "Phone number is required")
                @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
                @Pattern(
                        regexp = "^0[0-9]{9}$",
                        message = "Phone number is invalid. It should contain only digits and be exactly 10 digits long."
                )
                String phone,

                @NotBlank(message = "Country is required") @Size(max = 100, message = "Country name must be less than 100 characters") String country) {
}
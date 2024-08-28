package com.ansbeno.books_service.domain.order;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Embeddable
public record Customer(
            @NotBlank(message = "Customer Name is required") String username,
            @NotBlank(message = "Customer email is required") @Email String email,
            @Pattern(regexp = "^0\\d{9}$", message = "Phone number is invalid. It should contain only digits and be exactly 10 digits long.") @NotBlank(message = "Customer phone number is required") String phone) {
}
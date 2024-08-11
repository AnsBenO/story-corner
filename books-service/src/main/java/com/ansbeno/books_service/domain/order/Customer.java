package com.ansbeno.books_service.domain.order;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public record Customer(
            @NotBlank(message = "Customer Name is required") String name,
            @NotBlank(message = "Customer email is required") @Email String email,
            @NotBlank(message = "Customer phone number is required") String phone) {
}
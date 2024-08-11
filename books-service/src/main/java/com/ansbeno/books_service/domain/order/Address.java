package com.ansbeno.books_service.domain.order;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public record Address(
            @NotBlank(message = "AddressLine1 is required") String addressLine1,
            String addressLine2,
            @NotBlank(message = "City is required") String city,
            @NotBlank(message = "State is required") String state,
            @NotBlank(message = "ZipCode is required") String zipCode,
            @NotBlank(message = "Country is required") String country) {
}
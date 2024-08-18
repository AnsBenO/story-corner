package com.ansbeno.books_service.domain.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderItemDTO(
            @NotBlank(message = "Code is required") String code,
            @NotBlank(message = "Name is required") String name,
            @NotNull(message = "Price is required") BigDecimal price,
            @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") Integer quantity) {
}

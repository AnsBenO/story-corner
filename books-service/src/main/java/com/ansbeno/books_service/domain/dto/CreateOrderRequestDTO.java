package com.ansbeno.books_service.dto;

import java.util.Set;

import com.ansbeno.books_service.domain.order.Address;
import com.ansbeno.books_service.domain.order.Customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record CreateOrderRequestDTO(
            @NotEmpty(message = "Items cannot be empty") Set<OrderItemDTO> items,
            @Valid Customer customer,
            @Valid Address deliveryAddress) {
}

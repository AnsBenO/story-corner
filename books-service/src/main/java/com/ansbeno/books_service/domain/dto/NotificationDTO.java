package com.ansbeno.books_service.domain.dto;

public record NotificationDTO(
            long id,
            boolean read,
            String message) {

}

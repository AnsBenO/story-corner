package com.ansbeno.books_service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import jakarta.validation.constraints.Min;

@ConfigurationProperties(prefix = "books")
public record ApplicationProperties(
            @DefaultValue("10") @Min(1) int pageSize) {

}

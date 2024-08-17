package com.ansbeno.books_service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import jakarta.validation.constraints.Min;

@ConfigurationProperties(prefix = "jwtsecurity")
public record SecurityProperties(
            @DefaultValue("15") @Min(1) int jwtExpirationMinutes,
            String jwtSecretKey) {
}

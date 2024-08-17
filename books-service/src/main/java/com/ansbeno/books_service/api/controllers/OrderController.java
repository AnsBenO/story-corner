package com.ansbeno.books_service.api.controllers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ansbeno.books_service.domain.exceptions.BookNotFoundException;
import com.ansbeno.books_service.domain.order.OrderService;
import com.ansbeno.books_service.domain.order.OrderSummary;
import com.ansbeno.books_service.dto.CreateOrderRequestDTO;
import com.ansbeno.books_service.dto.CreateOrderResponseDTO;
import com.ansbeno.books_service.dto.OrderDTO;
import com.ansbeno.books_service.security.SecurityService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {
      private final SecurityService securityService;
      private final OrderService orderService;

      @PostMapping
      @ResponseStatus(HttpStatus.CREATED)
      CreateOrderResponseDTO createNewOrder(@Valid @RequestBody CreateOrderRequestDTO request)
                  throws BookNotFoundException {
            String username = securityService.getLoginUsername();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            log.info("Authentication Authorities: {}", authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", ")));
            log.info("Creating Order for User {}", username);
            return orderService.createNewOrder(username, request);
      }

      @GetMapping
      List<OrderSummary> getOrders() {
            String username = securityService.getLoginUsername();
            log.info("Fetching orders for user {}", username);
            return orderService.findOrders(username);
      }

      @GetMapping("/{orderNumber}")
      OrderDTO getOrder(@PathVariable() String orderNumber) {
            log.info("Fetching order By orderNumber {}", orderNumber);
            String username = securityService.getLoginUsername();
            return orderService.findUserOrder(username, orderNumber);
      }

}

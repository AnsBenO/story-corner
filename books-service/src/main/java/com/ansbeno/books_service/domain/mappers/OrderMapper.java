package com.ansbeno.books_service.domain.mappers;

import java.util.stream.Collectors;

import com.ansbeno.books_service.domain.order.OrderEntity;

import com.ansbeno.books_service.domain.dto.OrderDTO;

public class OrderMapper {
      OrderMapper() {
      }

      public static OrderDTO mapToOrderDTO(OrderEntity order) {
            return OrderDTO.builder()
                        .orderNumber(order.getOrderNumber())
                        .user(order.getUsername())
                        .items(order.getItems().stream().map(OrderItemMapper::mapToOrderItemDTO)
                                    .collect(Collectors.toSet()))
                        .customer(order.getCustomer())
                        .deliveryAddress(order.getDeliveryAddress())
                        .status(order.getStatus())
                        .comments(order.getComments())
                        .createdAt(order.getCreatedAt()).build();
      }
}

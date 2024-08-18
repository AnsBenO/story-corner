package com.ansbeno.books_service.domain.mappers;

import com.ansbeno.books_service.domain.order.OrderEntity;
import com.ansbeno.books_service.domain.orderitem.OrderItem;
import com.ansbeno.books_service.domain.dto.OrderItemDTO;

public class OrderItemMapper {

      OrderItemMapper() {
      }

      public static OrderItem mapToOrderItemEntity(OrderItemDTO orderItem, OrderEntity newOrder) {

            return OrderItem.builder()
                        .code(orderItem.code())
                        .name(orderItem.name())
                        .price(orderItem.price())
                        .order(newOrder)
                        .quantity(orderItem.quantity()).build();

      }

      public static OrderItemDTO mapToOrderItemDTO(OrderItem orderItem) {

            return OrderItemDTO.builder()
                        .code(orderItem.getCode())
                        .name(orderItem.getName())
                        .price(orderItem.getPrice())
                        .quantity(orderItem.getQuantity()).build();

      }
}

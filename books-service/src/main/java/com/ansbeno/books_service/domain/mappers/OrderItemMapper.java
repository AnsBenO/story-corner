package com.ansbeno.books_service.domain.mappers;

import com.ansbeno.books_service.domain.order.OrderEntity;
import com.ansbeno.books_service.domain.orderitem.OrderItemEntity;
import com.ansbeno.books_service.domain.dto.OrderItemDTO;

public class OrderItemMapper {

      OrderItemMapper() {
      }

      public static OrderItemEntity mapToOrderItemEntity(OrderItemDTO orderItem, OrderEntity newOrder) {

            return OrderItemEntity.builder()
                        .code(orderItem.code())
                        .name(orderItem.name())
                        .price(orderItem.price())
                        .order(newOrder)
                        .quantity(orderItem.quantity()).build();

      }

      public static OrderItemDTO mapToOrderItemDTO(OrderItemEntity orderItem) {

            return OrderItemDTO.builder()
                        .code(orderItem.getCode())
                        .name(orderItem.getName())
                        .price(orderItem.getPrice())
                        .quantity(orderItem.getQuantity()).build();

      }
}

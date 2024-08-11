package com.ansbeno.books_service.mappers;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ansbeno.books_service.domain.order.Order;
import com.ansbeno.books_service.domain.order.OrderStatus;
import com.ansbeno.books_service.domain.orderitem.OrderItem;
import com.ansbeno.books_service.dto.CreateOrderRequestDTO;
import com.ansbeno.books_service.dto.OrderDTO;

public class OrderMapper {
      OrderMapper() {
      }

      public static Order mapRequestToEntity(CreateOrderRequestDTO request) {
            Order newOrder = Order.builder()
                        .orderNumber(UUID.randomUUID().toString())
                        .status(OrderStatus.NEW)
                        .customer(request.customer())
                        .deliveryAddress(request.deliveryAddress())
                        .build();

            Set<OrderItem> orderItems = request.items()
                        .stream()
                        .map(item -> OrderItemMapper.mapToOrderItemEntity(item, newOrder))
                        .collect(Collectors.toSet());

            newOrder.setItems(orderItems);

            return newOrder;
      }

      public static OrderDTO mapToOrderDTO(Order order) {
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

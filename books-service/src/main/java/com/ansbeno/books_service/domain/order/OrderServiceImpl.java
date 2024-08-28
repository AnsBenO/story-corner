package com.ansbeno.books_service.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ansbeno.books_service.domain.exceptions.BookNotFoundException;
import com.ansbeno.books_service.domain.exceptions.InvalidOrderException;
import com.ansbeno.books_service.domain.exceptions.OrderNotFoundException;
import com.ansbeno.books_service.domain.orderitem.OrderItem;
import com.ansbeno.books_service.domain.dto.CreateOrderRequestDTO;
import com.ansbeno.books_service.domain.dto.CreateOrderResponseDTO;
import com.ansbeno.books_service.domain.dto.OrderDTO;
import com.ansbeno.books_service.domain.mappers.OrderItemMapper;
import com.ansbeno.books_service.domain.mappers.OrderMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

      private final OrderRepository orderRepository;
      private final OrderValidator orderValidator;
      // private final OrderStatusNotificationService notificationService;

      private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("USA", "GERMANY", "UK");

      @Override
      public OrderDTO findUserOrder(String userName, String orderNumber) throws OrderNotFoundException {
            Optional<OrderEntity> orderOptional = orderRepository.findByUsernameAndOrderNumber(userName, orderNumber);
            if (orderOptional.isPresent()) {
                  OrderEntity order = orderOptional.get();
                  return OrderMapper.mapToOrderDTO(order);
            }
            throw OrderNotFoundException.forOrderNumber(orderNumber);
      }

      @Override
      public List<OrderSummary> findOrders(String username) {
            return orderRepository.findByUsername(username);
      }

      @Override
      public CreateOrderResponseDTO createNewOrder(String userName, CreateOrderRequestDTO request) {

            try {
                  orderValidator.validate(request);
            } catch (BookNotFoundException | InvalidOrderException e) {
                  log.error("Order validation failed for user {}: {}", userName, e.getMessage());
                  throw new InvalidOrderException(e.getMessage());
            }

            OrderEntity order = OrderEntity.builder()
                        .orderNumber(generateOrderNumber())
                        .username(userName)
                        .status(OrderStatus.NEW)
                        .deliveryAddress(request.deliveryAddress())
                        .customer(new Customer(userName, userName, userName))
                        .createdAt(LocalDateTime.now())
                        .build();

            Set<OrderItem> orderItems = request.items().stream()
                        .map(item -> OrderItemMapper.mapToOrderItemEntity(item, order))
                        .collect(Collectors.toSet());

            order.setItems(orderItems);

            orderRepository.save(order);
            log.info("Created new order {} for user {}", order.getOrderNumber(), userName);

            // Process the order immediately
            process(order);

            return new CreateOrderResponseDTO(order.getOrderNumber());
      }

      private void process(OrderEntity order) {
            if (!canBeDelivered(order)) {
                  log.warn("Order {} cannot be delivered to {}", order.getOrderNumber(),
                              order.getDeliveryAddress().country());
                  orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                  // notificationService.notifyOrderStatus(order.getUsername(),
                  // order.getOrderNumber(), OrderStatus.CANCELLED);
                  return;
            }

            log.info("Processing payment for order {}", order.getOrderNumber());
            // Add payment processing logic here
            boolean paymentSuccessful = true; // Assume payment is successful

            if (paymentSuccessful) {
                  log.info("Order {} has been successfully processed", order.getOrderNumber());
                  orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERY_IN_PROGRESS);
                  // notificationService.notifyOrderStatus(order.getUsername(),
                  // order.getOrderNumber(), OrderStatus.DELIVERY_IN_PROGRESS);
            } else {
                  orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                  // notificationService.notifyOrderStatus(order.getUsername(),
                  // order.getOrderNumber(), OrderStatus.CANCELLED);
            }
      }

      private boolean canBeDelivered(OrderEntity order) {
            return DELIVERY_ALLOWED_COUNTRIES.contains(
                        order.getDeliveryAddress().country().toUpperCase());
      }

      private String generateOrderNumber() {
            return "ORD-" + System.currentTimeMillis();
      }
}

package com.ansbeno.books_service.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ansbeno.books_service.domain.exceptions.BookNotFoundException;
import com.ansbeno.books_service.domain.exceptions.InvalidOrderException;
import com.ansbeno.books_service.domain.exceptions.OrderNotFoundException;

import com.ansbeno.books_service.domain.orderitem.OrderItemEntity;
import com.ansbeno.books_service.domain.user.UserEntity;
import com.ansbeno.books_service.domain.user.UserRepository;
import com.ansbeno.books_service.domain.dto.CreateOrderRequestDTO;
import com.ansbeno.books_service.domain.dto.CreateOrderResponseDTO;
import com.ansbeno.books_service.domain.dto.OrderDTO;
import com.ansbeno.books_service.domain.mappers.OrderItemMapper;
import com.ansbeno.books_service.domain.mappers.OrderMapper;
import com.ansbeno.books_service.domain.notification.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

      private final OrderRepository orderRepository;
      private final OrderValidator orderValidator;
      private final UserRepository userRepository;
      private final NotificationService notificationService;

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

            UserEntity user = userRepository.findByUsername(userName).orElseThrow(() -> {
                  throw new UsernameNotFoundException("No user with username" + userName);
            });
            try {
                  orderValidator.validate(request);
            } catch (BookNotFoundException | InvalidOrderException e) {
                  log.error("Order validation failed for user {}: {}", user.getUsername(), e.getMessage());
                  throw new InvalidOrderException(e.getMessage());
            }

            OrderEntity order = OrderEntity.builder()
                        .orderNumber(generateOrderNumber())
                        .user(user)
                        .status(OrderStatus.NEW)
                        .deliveryAddress(request.deliveryAddress())
                        .customer(new Customer(user.getUsername(), user.getEmail(), user.getPhone()))
                        .createdAt(LocalDateTime.now())
                        .build();

            Set<OrderItemEntity> orderItems = request.items().stream()
                        .map(item -> OrderItemMapper.mapToOrderItemEntity(item, order))
                        .collect(Collectors.toSet());

            order.setItems(orderItems);

            orderRepository.save(order);
            log.info("Created new order {} for user {}", order.getOrderNumber(), user.getUsername());

            // Process the order
            process(order);

            return new CreateOrderResponseDTO(order.getOrderNumber());
      }

      private void process(OrderEntity order) {
            if (!canBeDelivered(order)) {
                  log.warn("Order {} cannot be delivered to {}", order.getOrderNumber(),
                              order.getDeliveryAddress().country());
                  orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                  notificationService.notifyOrderStatus(order, OrderStatus.CANCELLED);
                  return;
            }

            log.info("Processing payment for order {}", order.getOrderNumber());

            boolean paymentSuccessful = true;

            if (paymentSuccessful) {
                  log.info("Order {} has been successfully processed", order.getOrderNumber());
                  orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERY_IN_PROGRESS);
                  notificationService.notifyOrderStatus(order, OrderStatus.DELIVERY_IN_PROGRESS);
            } else {
                  orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                  notificationService.notifyOrderStatus(order, OrderStatus.CANCELLED);
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

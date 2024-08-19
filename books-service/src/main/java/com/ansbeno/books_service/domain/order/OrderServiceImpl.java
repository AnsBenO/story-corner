package com.ansbeno.books_service.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;

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

      private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("USA", "GERMANY", "UK");


      @Override
      public OrderDTO findUserOrder(String userName, String orderNumber) throws OrderNotFoundException {
            return orderRepository.findByUsernameAndOrderNumber(userName, orderNumber)
                    .map(OrderMapper::mapToOrderDTO)
                    .orElseThrow(() -> OrderNotFoundException.forOrderNumber(orderNumber));
      }

      @Override
      public CreateOrderResponseDTO createNewOrder(String userName, CreateOrderRequestDTO request)
              throws InvalidOrderException, BookNotFoundException {

            if (!canBeDelivered(request.deliveryAddress().country())) {
                  throw new InvalidOrderException("Delivery not allowed to this country");
            }
            orderValidator.validate(request);

            OrderStatus orderStatus = determineOrderStatus(request);

            OrderEntity order = OrderEntity.builder()
                    .orderNumber(generateOrderNumber())
                    .username(userName)
                    .status(orderStatus)
                    .deliveryAddress(request.deliveryAddress())
                    .customer(request.customer())
                    .createdAt(LocalDateTime.now())
                    .build();

            Set<OrderItem> orderItems = request.items().stream()
                    .map(item -> OrderItemMapper.mapToOrderItemEntity(item, order))
                    .collect(Collectors.toSet());

            order.setItems(orderItems);

            orderRepository.save(order);
            log.info("Created new order {} for user {}", order.getOrderNumber(), userName);

            return new CreateOrderResponseDTO(order.getOrderNumber());
      }

      private boolean canBeDelivered(String country) {
            return DELIVERY_ALLOWED_COUNTRIES.contains(country.toUpperCase());
      }

      private String generateOrderNumber() {
            return UUID.randomUUID().toString();
      }

      private OrderStatus determineOrderStatus(CreateOrderRequestDTO request) {
            return canBeDelivered(request.deliveryAddress().country()) ? OrderStatus.DELIVERY_IN_PROGRESS : OrderStatus.CANCELLED;
      }

      @Override
      public List<OrderSummary> findOrders(String username) {
            return orderRepository.findByUsername(username);
      }
}

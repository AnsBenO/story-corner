package com.ansbeno.books_service.domain.order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {

      List<OrderEntity> findByStatus(OrderStatus status);

      Optional<OrderEntity> findByOrderNumber(String orderNumber);

      default void updateOrderStatus(String orderNumber, OrderStatus status) {
            OrderEntity order = this.findByOrderNumber(orderNumber).orElseThrow();
            order.setStatus(status);
            this.save(order);
      }

      @Query("""
                        SELECT NEW com.ansbeno.books_service.domain.order.OrderSummary(o.orderNumber, o.status)
                        FROM OrderEntity o
                        WHERE o.username = :username
                  """)
      List<OrderSummary> findByUsername(String username);

      @Query("""
                        SELECT DISTINCT o
                        FROM OrderEntity o LEFT JOIN FETCH o.items
                        WHERE o.username = :username AND o.orderNumber = :orderNumber
                  """)
      Optional<OrderEntity> findByUsernameAndOrderNumber(String username, String orderNumber);

}

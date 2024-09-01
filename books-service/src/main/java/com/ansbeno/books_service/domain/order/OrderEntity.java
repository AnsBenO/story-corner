package com.ansbeno.books_service.domain.order;

import java.time.LocalDateTime;
import java.util.Set;

import com.ansbeno.books_service.domain.orderitem.OrderItemEntity;
import com.ansbeno.books_service.domain.user.UserEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_generator")
      @SequenceGenerator(name = "order_id_generator", sequenceName = "order_id_seq")
      private Long id;

      @Column(nullable = false, unique = true)
      private String orderNumber;

      @ManyToOne(optional = false)
      @JoinColumn(name = "user_id")
      private UserEntity user;

      @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
      private Set<OrderItemEntity> items;

      @Embedded
      @AttributeOverrides(value = {
                  @AttributeOverride(name = "username", column = @Column(name = "customer_name")),
                  @AttributeOverride(name = "email", column = @Column(name = "customer_email")),
                  @AttributeOverride(name = "phone", column = @Column(name = "customer_phone"))
      })
      private Customer customer;

      @Embedded
      @AttributeOverrides(value = {
                  @AttributeOverride(name = "addressLine1", column = @Column(name = "delivery_address_line1")),
                  @AttributeOverride(name = "addressLine2", column = @Column(name = "delivery_address_line2")),
                  @AttributeOverride(name = "city", column = @Column(name = "delivery_address_city")),
                  @AttributeOverride(name = "state", column = @Column(name = "delivery_address_state")),
                  @AttributeOverride(name = "zipCode", column = @Column(name = "delivery_address_zip_code")),
                  @AttributeOverride(name = "country", column = @Column(name = "delivery_address_country")),
      })
      private Address deliveryAddress;

      @Enumerated(EnumType.STRING)
      private OrderStatus status;

      private String comments;

      @Builder.Default
      @Column(name = "created_at", nullable = false, updatable = false)
      private LocalDateTime createdAt = LocalDateTime.now();

      @Column(name = "updated_at")
      private LocalDateTime updatedAt;
}

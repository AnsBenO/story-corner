package com.ansbeno.books_service.domain.orderitem;

import java.math.BigDecimal;

import com.ansbeno.books_service.domain.order.OrderEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItemEntity {

      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_id_generator")
      @SequenceGenerator(name = "order_item_id_generator", sequenceName = "order_item_id_seq")
      private Long id;

      @Column(nullable = false)
      private String code;

      @Column(nullable = false)
      private String name;

      @Column(nullable = false)
      private BigDecimal price;

      @Column(nullable = false)
      private Integer quantity;

      @ManyToOne(optional = false)
      @JoinColumn(name = "order_id")
      private OrderEntity order;

}
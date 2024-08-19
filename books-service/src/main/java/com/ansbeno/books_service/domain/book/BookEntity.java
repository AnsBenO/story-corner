package com.ansbeno.books_service.domain.book;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "books")
public class BookEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_generator")
  @SequenceGenerator(name = "book_id_generator", sequenceName = "book_id_seq")
  private Long id;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String name;

  private String description;

  private String imageUrl;

  @Column(nullable = false)
  private BigDecimal price;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;
}
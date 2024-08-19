package com.ansbeno.books_service.domain.user;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
      @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq")
      private Long id;

      @Column(unique = true)
      private String username;

      @Column(unique = true)
      private String email;

      private String password;

      @Column(unique = true)
      private String country;

      private String phone;

      @Enumerated(EnumType.STRING)
      private Role role;

      @CreationTimestamp
      @Column(nullable = false)
      private LocalDateTime createdAt;

      @UpdateTimestamp
      @Column(nullable = false)
      private LocalDateTime updatedAt;
}

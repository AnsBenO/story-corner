package com.ansbeno.books_service.domain.notification;

import java.time.LocalDateTime;

import com.ansbeno.books_service.domain.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
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
@Table(name = "notifications")
public class NotificationEntity {

      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id_generator")
      @SequenceGenerator(name = "notification_id_generator", sequenceName = "notification_id_seq", allocationSize = 50)
      private Long id;

      @ManyToOne(fetch = FetchType.LAZY, optional = false)
      @JoinColumn(name = "user_id", nullable = false)
      private UserEntity user;

      @Column(name = "read", nullable = false)
      private boolean read;

      private String message;

      @Builder.Default
      @Column(name = "created_at", nullable = false, updatable = false)
      private LocalDateTime createdAt = LocalDateTime.now();

      @Builder.Default
      @Column(name = "updated_at")
      private LocalDateTime updatedAt = LocalDateTime.now();

      @PreUpdate
      public void preUpdate() {
            updatedAt = LocalDateTime.now();
      }
}
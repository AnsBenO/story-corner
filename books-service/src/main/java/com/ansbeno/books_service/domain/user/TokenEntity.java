package com.ansbeno.books_service.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tokens")
public class TokenEntity {
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
      @SequenceGenerator(name = "token_seq", sequenceName = "token_id_seq", allocationSize = 1)
      private Long id;

      @Column(nullable = false, unique = true)
      private String token;

      @ManyToOne(optional = false)
      @JoinColumn(name = "user_id")
      UserEntity user;

      @Column(name = "token_type", nullable = false)
      @Enumerated(EnumType.STRING)
      private TokenType tokenType;

      @Builder.Default
      private boolean revoked = false;

      @Builder.Default
      private int usageCount = 0;

}

package com.ansbeno.books_service.domain.user;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

      Optional<TokenEntity> findByToken(String token);

      boolean existsByTokenAndRevoked(String token, boolean revoked);

      Set<TokenEntity> findByUser(UserEntity user);

}

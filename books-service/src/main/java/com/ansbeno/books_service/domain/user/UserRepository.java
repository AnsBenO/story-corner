package com.ansbeno.books_service.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}

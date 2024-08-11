package com.ansbeno.books_service.domain.book;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface BookRepository extends JpaRepository<BookEntity, Long> {

      Optional<BookEntity> findByCode(String code);

      void deleteByCode(String code);
}
package com.ansbeno.books_service.domain.mappers;

import com.ansbeno.books_service.domain.book.BookEntity;
import com.ansbeno.books_service.domain.dto.BookDto;

public class BookMapper {
      private BookMapper() {
      }

      public static BookEntity mapToBookEntity(BookDto book) {
            if (book == null) {
                  return null;
            }
            return BookEntity.builder()
                        .id(book.getId())
                        .code(book.getCode())
                        .name(book.getName())
                        .description(book.getDescription())
                        .imageUrl(book.getImageUrl())
                        .price(book.getPrice())
                        .createdAt(book.getCreatedAt())
                        .updatedAt(book.getUpdatedAt())
                        .build();
      }

      public static BookDto mapToBookDto(BookEntity book) {
            if (book == null) {
                  return null;
            }
            return BookDto.builder()
                        .id(book.getId())
                        .code(book.getCode())
                        .name(book.getName())
                        .description(book.getDescription())
                        .imageUrl(book.getImageUrl())
                        .price(book.getPrice())
                        .createdAt(book.getCreatedAt())
                        .updatedAt(book.getUpdatedAt())
                        .build();
      }
}

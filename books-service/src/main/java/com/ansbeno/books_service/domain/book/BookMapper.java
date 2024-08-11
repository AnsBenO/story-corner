package com.ansbeno.books_service.domain.book;

import com.ansbeno.books_service.dto.BookDto;

class BookMapper {
      private BookMapper() {
      }

      static BookEntity mapToBookEntity(BookDto book) {
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

      static BookDto mapToBookDto(BookEntity book) {
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

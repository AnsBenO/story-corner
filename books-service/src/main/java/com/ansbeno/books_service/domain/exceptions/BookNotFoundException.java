package com.ansbeno.books_service.domain.exceptions;

public class BookNotFoundException extends RuntimeException {
      public BookNotFoundException(String message) {
            super(message);
      }

      public static BookNotFoundException forBookCode(String bookCode) {
            return new BookNotFoundException("Book with Number " + bookCode + " Not Found");
      }
}

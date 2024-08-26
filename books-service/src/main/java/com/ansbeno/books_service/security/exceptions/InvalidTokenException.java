package com.ansbeno.books_service.security.exceptions;

public class InvalidTokenException extends RuntimeException {
      public InvalidTokenException(String message) {
            super(message);
      }
}

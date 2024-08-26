package com.ansbeno.books_service.security.exceptions;

public class TokenNotFoundException extends RuntimeException {
      public TokenNotFoundException(String message) {
            super(message);
      }
}

package com.ansbeno.books_service.security.exceptions;

public class TokenExpiredException extends RuntimeException {

      public TokenExpiredException(String message) {
            super(message);
      }
}
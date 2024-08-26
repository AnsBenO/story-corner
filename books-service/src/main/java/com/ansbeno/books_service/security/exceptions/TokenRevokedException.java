package com.ansbeno.books_service.security.exceptions;

public class TokenRevokedException extends RuntimeException {

      public TokenRevokedException(String message) {
            super(message);
      }
}
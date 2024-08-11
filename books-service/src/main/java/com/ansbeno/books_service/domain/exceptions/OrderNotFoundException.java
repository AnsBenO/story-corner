package com.ansbeno.books_service.domain.exceptions;

public class OrderNotFoundException extends RuntimeException {
      public OrderNotFoundException(String message) {
            super(message);
      }

      public static OrderNotFoundException forOrderNumber(String orderNumber) {
            return new OrderNotFoundException("Order with Number " + orderNumber + " Not Found");
      }
}

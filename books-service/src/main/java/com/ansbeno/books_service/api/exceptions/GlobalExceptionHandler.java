package com.ansbeno.books_service.api.exceptions;

import java.net.URI;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javassist.NotFoundException;

@ControllerAdvice
class GlobalExceptionHandler {
      private static final URI NOT_FOUND_TYPE = URI.create("https://api.bookstore.com/errors/not-found");
      private static final URI ISE_FOUND_TYPE = URI.create("https://api.bookstore.com/errors/server-error");
      private static final String SERVICE_NAME = "books-service";

      @ExceptionHandler(NotFoundException.class)
      ProblemDetail handleNotFoundException(NotFoundException ex, WebRequest request) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
            problemDetail.setTitle("Book Not Found");
            problemDetail.setType(NOT_FOUND_TYPE);
            problemDetail.setProperty("service", SERVICE_NAME);
            problemDetail.setProperty("error_category", "Generic");
            problemDetail.setProperty("timestamp", Instant.now());
            return problemDetail;
      }

      @ExceptionHandler(Exception.class)
      ProblemDetail handleUnhandledException(Exception e) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                        e.getMessage());
            problemDetail.setTitle("Internal Server Error");
            problemDetail.setType(ISE_FOUND_TYPE);
            problemDetail.setProperty("service", SERVICE_NAME);
            problemDetail.setProperty("error_category", "Generic");
            problemDetail.setProperty("timestamp", Instant.now());
            return problemDetail;
      }
}
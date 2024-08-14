package com.ansbeno.books_service.api.exceptions;

import java.net.URI;
import java.time.Instant;

import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.ansbeno.books_service.domain.exceptions.BookNotFoundException;
import com.ansbeno.books_service.domain.exceptions.InvalidOrderException;
import com.ansbeno.books_service.domain.exceptions.OrderNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
class GlobalExceptionHandler {
      private static final URI NOT_FOUND_TYPE = URI.create("https://api.story-corner.com/errors/not-found");
      private static final URI ISE_FOUND_TYPE = URI.create("https://api.story-corner.com/errors/server-error");
      private static final String SERVICE_NAME = "books-service";
      private static final URI BAD_REQUEST_TYPE = URI.create("https://api.story-corner.com/errors/bad-request");

      @ExceptionHandler(BookNotFoundException.class)
      ProblemDetail handleNotFoundException(BookNotFoundException ex, WebRequest request) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
            problemDetail.setTitle("Book Not Found");
            problemDetail.setType(NOT_FOUND_TYPE);
            problemDetail.setProperty("service", SERVICE_NAME);
            problemDetail.setProperty("error_category", "Generic");
            problemDetail.setProperty("timestamp", Instant.now());
            return problemDetail;
      }

      @ExceptionHandler(OrderNotFoundException.class)
      ProblemDetail handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
            problemDetail.setTitle("Order Not Found");
            problemDetail.setType(NOT_FOUND_TYPE);
            problemDetail.setProperty("service", SERVICE_NAME);
            problemDetail.setProperty("error_category", "Generic");
            problemDetail.setProperty("timestamp", Instant.now());
            return problemDetail;
      }

      @ExceptionHandler(Exception.class)
      ProblemDetail handleUnhandledException(Exception e) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Something Bad Happened");
            problemDetail.setTitle("Internal Server Error");
            problemDetail.setType(ISE_FOUND_TYPE);
            problemDetail.setProperty("service", SERVICE_NAME);
            problemDetail.setProperty("error_category", "Generic");
            problemDetail.setProperty("timestamp", Instant.now());
            return problemDetail;
      }

      @ExceptionHandler(InvalidOrderException.class)
      ProblemDetail handleInvalidOrderException(InvalidOrderException ex) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
            problemDetail.setTitle("Invalid Order Request");
            problemDetail.setType(BAD_REQUEST_TYPE);
            problemDetail.setProperty("service", SERVICE_NAME);
            problemDetail.setProperty("error_category", "Validation");
            problemDetail.setProperty("timestamp", Instant.now());
            return problemDetail;
      }

      @ResponseStatus(HttpStatus.BAD_REQUEST)
      @ExceptionHandler(MethodArgumentNotValidException.class)
      public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                  String fieldName = ((FieldError) error).getField();
                  String errorMessage = error.getDefaultMessage();
                  errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
      }
}
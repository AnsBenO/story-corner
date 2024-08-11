package com.ansbeno.books_service.api.exceptions;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ansbeno.books_service.domain.exceptions.OrderNotFoundException;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
      private static final URI NOT_FOUND_TYPE = URI.create("https://api.bookstore.com/errors/not-found");
      private static final URI ISE_FOUND_TYPE = URI.create("https://api.bookstore.com/errors/server-error");
      private static final String SERVICE_NAME = "books-service";
      private static final URI BAD_REQUEST_TYPE = URI.create("https://api.bookstore.com/errors/bad-request");

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

      @ExceptionHandler(OrderNotFoundException.class)
      ProblemDetail handleNotFoundException(OrderNotFoundException ex, WebRequest request) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
            problemDetail.setTitle("Book Not Found");
            problemDetail.setType(NOT_FOUND_TYPE);
            problemDetail.setProperty("service", SERVICE_NAME);
            problemDetail.setProperty("error_category", "Generic");
            problemDetail.setProperty("timestamp", Instant.now());
            return problemDetail;
      }

      @Override
      @Nullable
      protected ResponseEntity<Object> handleMethodArgumentNotValid(
                  @NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
                  @NonNull HttpStatusCode status, @NonNull WebRequest request) {
            List<String> errors = new ArrayList<>();
            ex.getBindingResult().getAllErrors().forEach(error -> {
                  String errorMessage = error.getDefaultMessage();
                  errors.add(errorMessage);
            });
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        "Invalid request payload");
            problemDetail.setTitle("Bad Request");
            problemDetail.setType(BAD_REQUEST_TYPE);
            problemDetail.setProperty("errors", errors);
            problemDetail.setProperty("service", SERVICE_NAME);
            problemDetail.setProperty("error_category", "Generic");
            problemDetail.setProperty("timestamp", Instant.now());

            log.info("Bad Request", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
      }
}
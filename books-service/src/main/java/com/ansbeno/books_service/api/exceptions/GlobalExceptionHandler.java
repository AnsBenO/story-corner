package com.ansbeno.books_service.api.exceptions;

import java.net.URI;
import java.time.Instant;

import java.util.Map;
import java.util.HashMap;

import com.ansbeno.books_service.security.exceptions.UserRegistrationException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
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
      private static final URI UNAUTHORIZED_TYPE = URI.create("https://api.story-corner.com/errors/unauthorized");

      @ExceptionHandler(ExpiredJwtException.class)
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      public ProblemDetail handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            String message = "JWT expired " + ex.getClaims().getExpiration().toInstant().toEpochMilli() + " milliseconds ago at " + ex.getClaims().getExpiration() + ". Current time: " + Instant.now() + ". Allowed clock skew: 0 milliseconds.";
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", message, UNAUTHORIZED_TYPE, path);
      }

      @ExceptionHandler(BookNotFoundException.class)
      public ProblemDetail handleNotFoundException(BookNotFoundException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            return createErrorResponse(HttpStatus.NOT_FOUND, "Book Not Found", ex.getMessage(), NOT_FOUND_TYPE, path);
      }

      @ExceptionHandler(OrderNotFoundException.class)
      public ProblemDetail handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            return createErrorResponse(HttpStatus.NOT_FOUND, "Order Not Found", ex.getMessage(), NOT_FOUND_TYPE, path);
      }

      @ExceptionHandler(InvalidOrderException.class)
      public ProblemDetail handleInvalidOrderException(InvalidOrderException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Order Request", ex.getMessage(), BAD_REQUEST_TYPE, path);
      }

      @ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class })
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      public ProblemDetail handleInvalidCredentials(Exception ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "Access Denied", ex.getMessage(), UNAUTHORIZED_TYPE, path);
      }

      @ExceptionHandler(UserRegistrationException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public ProblemDetail handleUserRegistrationException(UserRegistrationException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            return createErrorResponse(HttpStatus.BAD_REQUEST, "User Registration Error", ex.getMessage(), BAD_REQUEST_TYPE, path);
      }

      @ExceptionHandler(HttpMessageNotReadableException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Request Body", "Malformed JSON request", BAD_REQUEST_TYPE, path);
      }

      @ExceptionHandler(Exception.class)
      public ProblemDetail handleUnhandledException(Exception e, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Something Bad Happened", ISE_FOUND_TYPE, path);
      }

      private ProblemDetail createErrorResponse(HttpStatus status, String title, String message, URI type, String path) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
            problemDetail.setTitle(title);
            problemDetail.setType(type);
            problemDetail.setProperty("service", SERVICE_NAME);
            problemDetail.setProperty("error_category", "Generic");
            problemDetail.setProperty("timestamp", Instant.now());
            problemDetail.setProperty("path", path);
            return problemDetail;
      }

      @ResponseStatus(HttpStatus.BAD_REQUEST)
      @ExceptionHandler({ MethodArgumentNotValidException.class })
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
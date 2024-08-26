package com.ansbeno.books_service.api.exceptions;

import java.net.URI;
import java.time.Instant;

import java.util.Map;
import java.util.HashMap;

import com.ansbeno.books_service.security.exceptions.InvalidTokenException;
import com.ansbeno.books_service.security.exceptions.TokenExpiredException;
import com.ansbeno.books_service.security.exceptions.TokenNotFoundException;
import com.ansbeno.books_service.security.exceptions.TokenRevokedException;
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
import org.springframework.web.bind.MissingRequestCookieException;
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
            Map<String, String> details = new HashMap<>();
            details.put("message", "JWT expired " + ex.getClaims().getExpiration().toInstant().toEpochMilli()
                        + " milliseconds ago at " + ex.getClaims().getExpiration() + ". Current time: " + Instant.now()
                        + ". Allowed clock skew: 0 milliseconds.");
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", details, UNAUTHORIZED_TYPE, path);
      }

      @ExceptionHandler(TokenRevokedException.class)
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      public ProblemDetail handleRevokedToken(TokenRevokedException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            Map<String, String> details = new HashMap<>();
            details.put("message", ex.getMessage());
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", details, UNAUTHORIZED_TYPE, path);
      }

      @ExceptionHandler(TokenExpiredException.class)
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      public ProblemDetail handleExpiredRefreshToken(TokenExpiredException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            Map<String, String> details = new HashMap<>();
            details.put("message", ex.getMessage());
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", details, UNAUTHORIZED_TYPE, path);
      }

      @ExceptionHandler(TokenNotFoundException.class)
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      public ProblemDetail handelTokenNotFound(TokenNotFoundException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            Map<String, String> details = new HashMap<>();
            details.put("message", ex.getMessage());
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", details, UNAUTHORIZED_TYPE, path);
      }

      @ExceptionHandler(InvalidTokenException.class)
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      public ProblemDetail handelInvalidToken(InvalidTokenException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            Map<String, String> details = new HashMap<>();
            details.put("message", ex.getMessage());
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", details, UNAUTHORIZED_TYPE, path);
      }

      @ExceptionHandler(MissingRequestCookieException.class)
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      public ProblemDetail handleMissingCookie(MissingRequestCookieException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            Map<String, String> details = new HashMap<>();
            details.put("message", ex.getMessage());
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", details, UNAUTHORIZED_TYPE, path);
      }

      @ExceptionHandler(BookNotFoundException.class)
      public ProblemDetail handleNotFoundException(BookNotFoundException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            Map<String, String> details = new HashMap<>();
            details.put("message", ex.getMessage());
            return createErrorResponse(HttpStatus.NOT_FOUND, "Book Not Found", details, NOT_FOUND_TYPE, path);
      }

      @ExceptionHandler(OrderNotFoundException.class)
      public ProblemDetail handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            Map<String, String> details = new HashMap<>();
            details.put("message", ex.getMessage());
            return createErrorResponse(HttpStatus.NOT_FOUND, "Order Not Found", details, NOT_FOUND_TYPE, path);
      }

      @ExceptionHandler(InvalidOrderException.class)
      public ProblemDetail handleInvalidOrderException(InvalidOrderException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            Map<String, String> details = new HashMap<>();
            details.put("message", ex.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Order Request", details, BAD_REQUEST_TYPE,
                        path);
      }

      @ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class })
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      public ProblemDetail handleInvalidCredentials(Exception ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            Map<String, String> details = new HashMap<>();
            details.put("message", ex.getMessage());
            return createErrorResponse(HttpStatus.UNAUTHORIZED, "Access Denied", details, UNAUTHORIZED_TYPE, path);
      }

      @ExceptionHandler(UserRegistrationException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public ProblemDetail handleUserRegistrationException(UserRegistrationException ex, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();

            Map<String, String> details = new HashMap<>();
            for (String error : ex.getMessage().split("/n")) {
                  String[] parts = error.split(" is ");
                  if (parts.length == 2) {
                        details.put(parts[0].trim(), error);
                  }
            }

            return createErrorResponse(HttpStatus.BAD_REQUEST, "User Registration Error", details, BAD_REQUEST_TYPE,
                        path);
      }

      @ExceptionHandler(HttpMessageNotReadableException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                  WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            Map<String, String> details = new HashMap<>();
            details.put("message", "Malformed JSON request");
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Request Body", details, BAD_REQUEST_TYPE, path);
      }

      @ExceptionHandler(Exception.class)
      public ProblemDetail handleUnhandledException(Exception e, WebRequest request) {
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            Map<String, String> details = new HashMap<>();
            details.put("message", "Something Bad Happened");
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", details,
                        ISE_FOUND_TYPE, path);
      }

      private ProblemDetail createErrorResponse(HttpStatus status, String title, Map<String, String> details, URI type,
                  String path) {
            ProblemDetail problemDetail = ProblemDetail.forStatus(status);
            problemDetail.setTitle(title);
            problemDetail.setType(type);
            problemDetail.setProperty("service", SERVICE_NAME);
            problemDetail.setProperty("error_category", "Generic");
            problemDetail.setProperty("timestamp", Instant.now());
            problemDetail.setProperty("path", path);
            problemDetail.setProperty("detail", details);
            return problemDetail;
      }

      @ResponseStatus(HttpStatus.BAD_REQUEST)
      @ExceptionHandler({ MethodArgumentNotValidException.class })
      public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex,
                  WebRequest request) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach(error -> {
                  String fieldName = ((FieldError) error).getField();
                  String errorMessage = error.getDefaultMessage();
                  errors.put(fieldName, errorMessage);
            });
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            ProblemDetail problemDetail = createErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", errors,
                        BAD_REQUEST_TYPE, path);
            return ResponseEntity.badRequest().body(problemDetail);
      }
}
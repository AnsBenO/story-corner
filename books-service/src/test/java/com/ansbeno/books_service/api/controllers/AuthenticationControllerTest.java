package com.ansbeno.books_service.api.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ansbeno.books_service.AbstractIntegrationTest;
import com.ansbeno.books_service.domain.user.Role;
import com.ansbeno.books_service.domain.user.UserEntity;
import com.ansbeno.books_service.domain.user.UserRepository;
import com.ansbeno.books_service.security.authentication.AuthenticationRequest;
import com.ansbeno.books_service.security.authentication.AuthenticationResponse;
import com.ansbeno.books_service.security.authentication.RegisterUserDto;

import io.restassured.http.ContentType;

public class AuthenticationControllerTest extends AbstractIntegrationTest {

      @Autowired
      private UserRepository userRepository;

      @Autowired
      PasswordEncoder passwordEncoder;

      @Nested
      class RegisterUserTests {
            @Test
            void shouldRegisterUserSuccessfully() {
                  var payload = new RegisterUserDto(
                              "newuser",
                              "newpassword",
                              "newuser@test.com",
                              "USA");

                  var response = given()
                              .contentType(ContentType.JSON)
                              .body(payload)
                              .when()
                              .post("/api/auth/register")
                              .then()
                              .statusCode(HttpStatus.OK.value())
                              .body("jwt", notNullValue())
                              .extract()
                              .as(AuthenticationResponse.class);

                  assertThat(response.jwt()).isNotNull();
                  // Verify user in database
                  UserEntity user = userRepository.findByUsername("newuser").orElse(null);
                  assertThat(user).isNotNull();
                  assertThat(user.getEmail()).isEqualTo("newuser@test.com");
            }

            @Test
            void shouldReturnBadRequestWhenRegistrationPayloadIsInvalid() {
                  var payload = new RegisterUserDto(
                              "", // Invalid username
                              "short", // Invalid password
                              "invalidemail", // Invalid email
                              "" // Invalid country
                  );

                  given().contentType(ContentType.JSON)
                              .body(payload)
                              .when()
                              .post("/api/auth/register")
                              .then()
                              .statusCode(HttpStatus.BAD_REQUEST.value());
            }
      }

      @Nested
      class LoginTests {
            @Test
            void shouldLoginSuccessfully() {

                  createTestUser();

                  var payload = new AuthenticationRequest(
                              "testuser",
                              "testpassword");

                  var response = given()
                              .contentType(ContentType.JSON)
                              .body(payload)
                              .when()
                              .post("/api/auth/login")
                              .then()
                              .statusCode(HttpStatus.OK.value())
                              .body("jwt", notNullValue())
                              .extract()
                              .as(AuthenticationResponse.class);

                  assertThat(response.jwt()).isNotNull();

            }

            private void createTestUser() {
                  UserEntity user = new UserEntity();
                  user.setUsername("testuser");
                  user.setPassword(passwordEncoder.encode("testpassword")); // Encode the password
                  user.setEmail("testuser@test.com");
                  user.setRole(Role.CUSTOMER);
                  user.setCountry("USA");
                  userRepository.save(user);
            }

            @Test
            void shouldReturnUnauthorizedForInvalidCredentials() {

                  var payload = new AuthenticationRequest(
                              "wronguser",
                              "wrongpassword");

                  given().contentType(ContentType.JSON)
                              .body(payload)
                              .when()
                              .post("/api/auth/login")
                              .then()
                              .statusCode(HttpStatus.UNAUTHORIZED.value());
            }
      }
}

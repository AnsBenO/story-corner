package com.ansbeno.books_service.api.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import com.ansbeno.books_service.security.authentication.AuthenticationRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ansbeno.books_service.AbstractIntegrationTest;
import com.ansbeno.books_service.domain.user.Role;
import com.ansbeno.books_service.domain.user.UserEntity;
import com.ansbeno.books_service.domain.user.UserRepository;
import com.ansbeno.books_service.security.authentication.AuthenticationResponse;
import com.ansbeno.books_service.security.authentication.CurrentUserResponseDto;
import com.ansbeno.books_service.security.authentication.RegisterUserDto;

import io.restassured.http.ContentType;

public class AuthenticationControllerTest extends AbstractIntegrationTest {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Nested
        class LoginTests {
                @Test
                void shouldLoginSuccessfully() {

                        createTestUser("testuser");

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

        @Nested
        class GetCurrentUserTests {

                @Test
                void shouldReturnCurrentUserDetails() {

                        createTestUser("testuser2");

                        // Log in to get the JWT token
                        var loginPayload = new AuthenticationRequest(
                                        "testuser2",
                                        "testpassword");

                        var loginResponse = given()
                                        .contentType(ContentType.JSON)
                                        .body(loginPayload)
                                        .when()
                                        .post("/api/auth/login")
                                        .then()
                                        .statusCode(HttpStatus.OK.value())
                                        .extract()
                                        .as(AuthenticationResponse.class);

                        // Use the token to get the current user details
                        var token = loginResponse.jwt();

                        var currentUserResponse = given()
                                        .header("Authorization", "Bearer " + token)
                                        .when()
                                        .get("/api/auth/user")
                                        .then()
                                        .statusCode(HttpStatus.OK.value())
                                        .extract()
                                        .as(CurrentUserResponseDto.class);

                        // Verify the returned user details
                        assertThat(currentUserResponse.username()).isEqualTo("testuser2");
                        assertThat(currentUserResponse.email()).isEqualTo("testuser2@test.com");
                        assertThat(currentUserResponse.phone()).isEqualTo("0634256790");
                }

        }

        @Nested
        class RegisterUserTests {
                @Test
                void shouldRegisterUserSuccessfully() {
                        var payload = new RegisterUserDto(
                                        "newuser",
                                        "newpassword",
                                        "newuser@test.com",
                                        "0345678903",
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
                        assertThat(user.getPhone()).isEqualTo("0345678903"); // Check the phone number
                }

                @Test
                void shouldReturnBadRequestWhenRegistrationPayloadIsInvalid() {
                        var payload = new RegisterUserDto(
                                        "", // Invalid username
                                        "short", // Invalid password
                                        "invalidemail", // Invalid email
                                        "invalidphone", // Invalid phone number
                                        "" // Invalid country
                        );

                        given().contentType(ContentType.JSON)
                                        .body(payload)
                                        .when()
                                        .post("/api/auth/register")
                                        .then()
                                        .statusCode(HttpStatus.BAD_REQUEST.value());
                }

                @Test
                void shouldReturnBadRequestForInvalidPhoneNumber() {
                        var payload = new RegisterUserDto(
                                        "validuser",
                                        "validpassword",
                                        "validuser@test.com",
                                        "123", // Too short phone number
                                        "USA");

                        given().contentType(ContentType.JSON)
                                        .body(payload)
                                        .when()
                                        .post("/api/auth/register")
                                        .then()
                                        .statusCode(HttpStatus.BAD_REQUEST.value());
                }
        }

        private void createTestUser(String username) {
                UserEntity user = new UserEntity();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode("testpassword")); // Encode the password
                user.setEmail(username + "@test.com");
                user.setPhone("0634256790");
                user.setRole(Role.CUSTOMER);
                user.setCountry("USA");
                userRepository.save(user);
        }
}
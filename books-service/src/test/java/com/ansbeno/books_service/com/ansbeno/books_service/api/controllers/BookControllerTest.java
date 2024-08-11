package com.ansbeno.books_service.com.ansbeno.books_service.api.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import com.ansbeno.books_service.AbstractIntegrationTest;
import com.ansbeno.books_service.dto.BookDto;

import io.restassured.http.ContentType;

@Sql("/test-data.sql")
class BookControllerTest extends AbstractIntegrationTest {
      @Test()
      void shouldReturnBooks() {
            given().contentType(ContentType.JSON)
                        .when()
                        .get("/api/books")
                        .then()
                        .statusCode(200)
                        .body("data", hasSize(10))
                        .body("totalElements", is(15))
                        .body("pageNumber", is(1))
                        .body("totalPages", is(2))
                        .body("isFirst", is(true))
                        .body("isLast", is(false))
                        .body("hasNext", is(true))
                        .body("hasPrevious", is(false));
      }

      @Test
      void shouldGetProductByCode() {
            BookDto book = given().contentType(ContentType.JSON)
                        .when()
                        .get("/api/books/{code}", "P100")
                        .then()
                        .statusCode(200)
                        .assertThat()
                        .extract()
                        .body()
                        .as(BookDto.class);

            assertThat(book.getCode(), is("P100"));
            assertThat(book.getName(), is("The Hunger Games"));
            assertThat(book.getDescription(), is("Winning will make you famous. Losing means certain death..."));
            assertThat(book.getPrice().stripTrailingZeros(),
                        comparesEqualTo(new BigDecimal("34.0").stripTrailingZeros()));

      }

      @Test
      void shouldReturnNotFoundWhenBookCodeNotExists() {
            String code = "invalid_Book_code";
            given().contentType(ContentType.JSON)
                        .when()
                        .get("/api/books/{code}", code)
                        .then()
                        .statusCode(404)
                        .body("status", is(404))
                        .body("title", is("Book Not Found"))
                        .body("detail", is("Book Not Found"));
      }
}

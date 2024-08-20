package com.ansbeno.books_service.api.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.ansbeno.books_service.domain.dto.CreateOrderRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import com.ansbeno.books_service.AbstractIntegrationTest;

import com.ansbeno.books_service.domain.order.OrderSummary;
import com.ansbeno.books_service.domain.user.UserEntity;
import com.ansbeno.books_service.domain.user.UserRepository;
import com.ansbeno.books_service.security.config.JwtService;
import com.ansbeno.books_service.testdata.TestDataFactory;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

@Sql("/test-orders.sql")
public class OrderControllerTest extends AbstractIntegrationTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    private String jwtToken;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        UserEntity user = userRepository.findByUsername("testuser")
                .orElseThrow(() -> new RuntimeException("Test user not found in the database"));

        jwtToken = jwtService.generateToken(user, Map.of("role", user.getRole().name()));
    }

    @Nested
    /**
     * CreateOrderTest
     */
    class CreateOrderTest {
        @Test
        void shouldCreateOrderSuccessfully() {
            var payload = """
                    {
                        "customer": {
                            "name": "Hello Geoe",
                            "email": "ans@email.com",
                            "phone": "999999999"
                        },
                        "deliveryAddress": {
                            "addressLine1": "Tetuan",
                            "addressLine2": "MA 23",
                            "city": "Tetuan",
                            "state": "MA",
                            "zipCode": "9019",
                            "country": "USA"
                        },
                        "items": [
                            {
                                "code": "P100",
                                "name": "The Hunger Games",
                                "price": 34.0,
                                "quantity": 1
                            }
                        ]
                    }
                                                                                          """;
            given().contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + jwtToken)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @ParameterizedTest(name = "[{index}]-{0}")
        @MethodSource("createOrderRequestProvider")
        void shouldReturnBadRequestWhenOrderPayloadIsInvalid(CreateOrderRequestDTO request) {
            given().contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + jwtToken)
                    .body(request)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        static Stream<Arguments> createOrderRequestProvider() {
            return Stream.of(
                    Arguments.arguments(Named.named("Order with Invalid Customer",
                            TestDataFactory.createOrderRequestWithInvalidCustomer())),
                    Arguments.arguments(Named.named("Order with Invalid Delivery Address",
                            TestDataFactory.createOrderRequestWithInvalidDeliveryAddress())),
                    Arguments.arguments(Named.named("Order with No Items",
                            TestDataFactory.createOrderRequestWithNoItems())));
        }

        @Test
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
            var payload = TestDataFactory.createOrderRequestWithInvalidDeliveryAddress();
            given().contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + jwtToken)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

    }

    @Nested
    class GetOrdersTests {
        @Test
        void shouldGetOrdersSuccessfully() {
            List<OrderSummary> orderSummaries = given()
                    .header("Authorization", "Bearer " + jwtToken)
                    .when()
                    .get("/api/orders")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(new TypeRef<>() {
                    });

            assertThat(orderSummaries).hasSize(2);

        }

        @Test
        void shouldReturnNotFoundWhenOrderNumberNotExists() {
            String number = "invalid_Order_code";
            given().contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + jwtToken)
                    .when()
                    .get("/api/orders/{code}", number)
                    .then()
                    .statusCode(404)
                    .body("status", is(404))
                    .body("title", is("Order Not Found"))
                    .body("detail", is("Order with Number " + number + " Not Found"));
        }
    }

    @Nested
    class GetOrderByOrderNumberTest {
        String orderNumber = "order-123";

        @Test
        void shouldGetOrderSuccessfully() {
            given()
                    .header("Authorization", "Bearer " + jwtToken)
                    .when()
                    .get("/api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(200)
                    .body("orderNumber", is(orderNumber))
                    .body("items.size()", is(2));
        }
    }
}
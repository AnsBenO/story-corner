package com.ansbeno.books_service.com.ansbeno.books_service.api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.ansbeno.books_service.api.controllers.OrderController;
import com.ansbeno.books_service.domain.order.OrderService;
import com.ansbeno.books_service.dto.CreateOrderRequestDTO;
import com.ansbeno.books_service.security.SecurityService;
import com.ansbeno.books_service.testdata.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrderController.class)
class OrderControllerUnitTest {
      @MockBean
      private OrderService orderService;

      @MockBean
      private SecurityService securityService;

      @Autowired
      private MockMvc mockMvc;

      @Autowired
      private ObjectMapper objectMapper;

      @ParameterizedTest(name = "[{index}]-{0}")
      @MethodSource("createOrderRequestProvider")
      void shouldReturnBadRequestWhenOrderPayloadIsInvalid(CreateOrderRequestDTO request) throws Exception {
            given(orderService.createNewOrder(eq("anas"), any(CreateOrderRequestDTO.class)))
                        .willReturn(null);

            mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/orders")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
      }

      static Stream<Arguments> createOrderRequestProvider() {
            return Stream.of(
                        arguments(named("OrderEntity with Invalid Customer",
                                    TestDataFactory.createOrderRequestWithInvalidCustomer())),
                        arguments(named("OrderEntity with Invalid Delivery Address",
                                    TestDataFactory.createOrderRequestWithInvalidDeliveryAddress())),
                        arguments(named("OrderEntity with No Items", TestDataFactory.createOrderRequestWithNoItems())));
      }

}

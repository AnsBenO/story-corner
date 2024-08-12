package com.ansbeno.books_service.testdata;

import org.instancio.Instancio;
import static org.instancio.Select.field;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.ansbeno.books_service.domain.order.Address;
import com.ansbeno.books_service.domain.order.Customer;
import com.ansbeno.books_service.dto.CreateOrderRequestDTO;
import com.ansbeno.books_service.dto.OrderItemDTO;

public class TestDataFactory {
      static final List<String> VALID_COUNTIES = List.of("India", "Germany");
      static final Set<OrderItemDTO> VALID_ORDER_ITEMS = Set
                  .of(new OrderItemDTO("P100", "Product 1", new BigDecimal("25.50"), 1));
      static final Set<OrderItemDTO> INVALID_ORDER_ITEMS = Set
                  .of(new OrderItemDTO("ABCD", "Product 1", new BigDecimal("25.50"), 1));

      public static CreateOrderRequestDTO createValidOrderRequest() {
            return Instancio.of(CreateOrderRequestDTO.class)
                        .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                        .set(field(CreateOrderRequestDTO::items), VALID_ORDER_ITEMS)
                        .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES))
                        .create();
      }

      public static CreateOrderRequestDTO createOrderRequestWithInvalidCustomer() {

            return Instancio.of(CreateOrderRequestDTO.class)
                        .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@mail.com"))
                        .set(field(Customer::phone), "")
                        .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES))
                        .set(field(CreateOrderRequestDTO::items), VALID_ORDER_ITEMS)
                        .create();
      }

      public static CreateOrderRequestDTO createOrderRequestWithInvalidDeliveryAddress() {
            return Instancio.of(CreateOrderRequestDTO.class)
                        .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@mail.com"))
                        .set(field(Address::country), "")
                        .set(field(CreateOrderRequestDTO::items), VALID_ORDER_ITEMS)
                        .create();
      }

      public static CreateOrderRequestDTO createOrderRequestWithNoItems() {
            return Instancio.of(CreateOrderRequestDTO.class)
                        .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@mail.com"))
                        .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES))
                        .set(field(CreateOrderRequestDTO::items), Set.of())
                        .create();
      }
}

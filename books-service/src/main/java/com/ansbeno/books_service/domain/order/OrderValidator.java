package com.ansbeno.books_service.domain.order;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.ansbeno.books_service.domain.book.BookService;
import com.ansbeno.books_service.domain.exceptions.InvalidOrderException;
import com.ansbeno.books_service.dto.BookDto;
import com.ansbeno.books_service.dto.CreateOrderRequestDTO;
import com.ansbeno.books_service.dto.OrderItemDTO;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
class OrderValidator {
      private final BookService bookService;

      void validate(CreateOrderRequestDTO request) throws NotFoundException {
            Set<OrderItemDTO> items = request.items();
            for (OrderItemDTO item : items) {
                  BookDto book = bookService.findByCode(item.code());
                  if (item.price().compareTo(book.getPrice()) != 0) {
                        log.error(
                                    "Book price not matching. Actual price:{}, received price:{}",
                                    book.getPrice(),
                                    item.price());
                        throw new InvalidOrderException("Book price not matching");
                  }
            }
      }
}

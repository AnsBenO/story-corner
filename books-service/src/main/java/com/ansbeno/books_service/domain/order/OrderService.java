package com.ansbeno.books_service.domain.order;

import java.util.List;

import com.ansbeno.books_service.domain.exceptions.OrderNotFoundException;
import com.ansbeno.books_service.dto.CreateOrderRequestDTO;
import com.ansbeno.books_service.dto.CreateOrderResponseDTO;
import com.ansbeno.books_service.dto.OrderDTO;

public interface OrderService {

      CreateOrderResponseDTO createNewOrder(String userName, CreateOrderRequestDTO request);

      List<OrderSummary> findOrders(String userName);

      OrderDTO findUserOrder(String userName, String orderNumber) throws OrderNotFoundException;

      void processNewOrders();

}

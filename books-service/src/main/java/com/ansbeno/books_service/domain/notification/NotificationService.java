package com.ansbeno.books_service.domain.notification;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ansbeno.books_service.domain.dto.NotificationDTO;
import com.ansbeno.books_service.domain.dto.PagedResultDto;
import com.ansbeno.books_service.domain.order.OrderEntity;
import com.ansbeno.books_service.domain.order.OrderStatus;

@Service
public interface NotificationService {

      void saveNotification(NotificationDTO notification, OrderEntity order);

      void markAsRead(List<Long> ids);

      void notifyOrderStatus(OrderEntity order, OrderStatus orderStatus);

      PagedResultDto<NotificationDTO> findAll(int page);
}

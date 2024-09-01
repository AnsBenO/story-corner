package com.ansbeno.books_service.domain.mappers;

import com.ansbeno.books_service.domain.dto.NotificationDTO;
import com.ansbeno.books_service.domain.notification.NotificationEntity;
import com.ansbeno.books_service.domain.order.OrderEntity;

public class NotificationMapper {
      private NotificationMapper() {
      }

      public static NotificationDTO mapToNotificationDto(NotificationEntity notification) {
            return new NotificationDTO(notification.getId(),
                        notification.isRead(),
                        notification.getMessage());
      }

      public static NotificationEntity mapToNotificationEntity(NotificationDTO notification, OrderEntity order) {

            return NotificationEntity.builder().user(order.getUser())
                        .id(notification.id())
                        .read(notification.read())
                        .message(notification.message())
                        .build();

      }
}

package com.ansbeno.books_service.domain.notification;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ansbeno.books_service.domain.dto.NotificationDTO;
import com.ansbeno.books_service.domain.dto.PagedResultDto;
import com.ansbeno.books_service.domain.mappers.NotificationMapper;
import com.ansbeno.books_service.domain.order.OrderEntity;
import com.ansbeno.books_service.domain.order.OrderStatus;
import com.ansbeno.books_service.security.SecurityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

      private final NotificationRepository notificationRepository;
      private final SimpMessagingTemplate messagingTemplate;
      private final SecurityService securityService;

      @Override
      public void saveNotification(NotificationDTO notification, OrderEntity order) {
            NotificationEntity notificationToSave = NotificationMapper.mapToNotificationEntity(notification, order);
            notificationRepository.save(notificationToSave);
      }

      @Override
      public void markAsRead(List<Long> ids) {
            ids.forEach(id -> {
                  NotificationEntity notification = notificationRepository.findById(id)
                              .orElseThrow(() -> new RuntimeException("Notification not found"));
                  notification.setRead(true);
                  notificationRepository.save(notification);
            });
      }

      @Override
      public void notifyOrderStatus(OrderEntity order, OrderStatus orderStatus) {
            // Create the notification
            String messageString;
            switch (orderStatus) {
                  case NEW: {
                        messageString = "Your order has been placed and is awaiting processing.";
                        break;
                  }
                  case IN_PROCESS: {
                        messageString = "Your order is being processed.";
                        break;
                  }
                  case DELIVERY_IN_PROGRESS: {
                        messageString = "Your order is on its way!";
                        break;
                  }
                  case DELIVERED: {
                        messageString = "Your order has been delivered.";
                        break;
                  }
                  case CANCELLED: {
                        messageString = "Your order has been cancelled.";
                        break;
                  }
                  case ERROR: {
                        messageString = "There was an issue with your order. Please contact support.";
                        break;
                  }
                  default: {
                        messageString = "Unknown order status.";
                        break;
                  }
            }

            NotificationEntity notificationToSave = NotificationEntity.builder().user(order.getUser())
                        .message(messageString).read(false).build();
            // Save the notification to the database
            NotificationEntity savedNotification = notificationRepository.save(notificationToSave);

            NotificationDTO notification = new NotificationDTO(savedNotification.getId(), false,
                        savedNotification.getMessage());
            String user = securityService.getLoginUsername();

            messagingTemplate.convertAndSend("/user/" + user + "/topic/notifications",
                        notification);
            log.info("notification sent with id {} to user {}", savedNotification.getId(), user);
      }

      @Override
      public PagedResultDto<NotificationDTO> findAll(int pageNumber) {
            pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;

            String user = securityService.getLoginUsername();

            // Fetch paginated data for the current user

            Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
            // Define page size and sorting
            Pageable pageable = PageRequest.of(pageNumber, 10, sort);
            // Fetch paginated data
            Page<NotificationEntity> notificationPage = notificationRepository.findNotificationsByUsername(user,
                        pageable);

            List<NotificationDTO> notifications = notificationPage.toList().stream()
                        .map(NotificationMapper::mapToNotificationDto).collect(Collectors.toList());

            return PagedResultDto.<NotificationDTO>builder()
                        .data(notifications)
                        .totalElements(notificationPage.getTotalElements())
                        .pageNumber(pageNumber + 1)
                        .totalPages(notificationPage.getTotalPages())
                        .isFirst(notificationPage.isFirst())
                        .isLast(notificationPage.isLast())
                        .hasNext(notificationPage.hasNext())
                        .hasPrevious(notificationPage.hasPrevious())
                        .build();
      }

}

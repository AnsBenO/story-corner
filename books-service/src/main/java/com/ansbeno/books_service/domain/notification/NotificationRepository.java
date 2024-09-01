package com.ansbeno.books_service.domain.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

      @Query("""
                  SELECT n FROM NotificationEntity n
                  JOIN n.user u
                  WHERE u.username = :username
                  """)
      Page<NotificationEntity> findNotificationsByUsername(String username, Pageable pageable);

}

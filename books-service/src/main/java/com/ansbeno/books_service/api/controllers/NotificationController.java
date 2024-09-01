package com.ansbeno.books_service.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ansbeno.books_service.domain.dto.NotificationDTO;
import com.ansbeno.books_service.domain.dto.PagedResultDto;
import com.ansbeno.books_service.domain.notification.NotificationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

      private final NotificationService notificationService;

      @PutMapping("/read")
      public void markAsRead(@RequestBody List<Long> ids) {
            notificationService.markAsRead(ids);
      }

      @GetMapping
      ResponseEntity<PagedResultDto<NotificationDTO>> getAllNotifications(
                  @RequestParam(defaultValue = "1") int page) {
            PagedResultDto<NotificationDTO> notifications = notificationService.findAll(page);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
      }

}
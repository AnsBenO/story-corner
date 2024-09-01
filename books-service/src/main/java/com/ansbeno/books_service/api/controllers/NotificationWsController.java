package com.ansbeno.books_service.api.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.ansbeno.books_service.domain.dto.NotificationDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class NotificationWsController {

      @MessageMapping("/order-status")
      @SendTo("/topic/notifications")
      public NotificationDTO sendNotification(NotificationDTO notification) {
            // Logic to send notification
            return notification;
      }
}
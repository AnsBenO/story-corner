package com.ansbeno.books_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
      @Override
      public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
            registry.enableSimpleBroker("/topic", "/user");
            registry.setApplicationDestinationPrefixes("/app");
            registry.setUserDestinationPrefix("/user");
      }

      @Override
      public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
            registry.addEndpoint("/inbox-socket")
                        .setAllowedOrigins("http://localhost:4200")
                        .withSockJS();
      }
}

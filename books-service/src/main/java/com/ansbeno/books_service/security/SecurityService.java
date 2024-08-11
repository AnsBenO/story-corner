package com.ansbeno.books_service.security;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

      public String getLoginUsername() {
            return "user";
      }

      public String getUserRole() {
            return "USER";
      }

}

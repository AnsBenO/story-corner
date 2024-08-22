package com.ansbeno.books_service.security.config;

import java.io.IOException;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
      private final JwtService jwtService;
      private final UserDetailsService userDetailsService;

      @Override
      protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                  @NonNull FilterChain filterChain)
                  throws ServletException, IOException {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || authHeader.isEmpty()
                        || !authHeader.startsWith("Bearer")) {
                  filterChain.doFilter(request, response);
                  return;
            }

            String jwt = authHeader.split(" ")[1];
            try {
                  String username = jwtService.extractUsernameFromAccessToken(jwt);

                  UserDetails user = userDetailsService.loadUserByUsername(username);

                  UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null,
                              user.getAuthorities());

                  SecurityContextHolder.getContext().setAuthentication(token);
                  filterChain.doFilter(request, response);
            } catch (ExpiredJwtException ex) {
                  final String expiredMsg = ex.getMessage();
                  logger.warn(expiredMsg);

                  final String msg = (expiredMsg != null) ? expiredMsg : "JWT token has expired";
                  response.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);
            }
      }
}

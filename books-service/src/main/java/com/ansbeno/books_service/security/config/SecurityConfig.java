package com.ansbeno.books_service.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ansbeno.books_service.domain.user.Permission;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
class SecurityConfig {
      private final JwtAuthenticationFilter jwtAuthenticationFilter;
      private final UserDetailsService userDetailsService;

      @Bean
      SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                        .csrf(AbstractHttpConfigurer::disable)
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authenticationProvider(authenticationProvider())
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                        .authorizeHttpRequests(
                                    authConfig -> {
                                          authConfig.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll();
                                          authConfig.requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll();
                                          authConfig.requestMatchers("/api/auth/user").authenticated();
                                          authConfig.requestMatchers("/api/auth/refresh-token").authenticated();
                                          authConfig.requestMatchers("/error").permitAll();
                                          authConfig.requestMatchers(HttpMethod.GET, "/api/books", "/api/books/*")
                                                      .permitAll();
                                          authConfig.requestMatchers(HttpMethod.POST, "/api/books")
                                                      .hasAnyAuthority(Permission.SAVE_ONE_PRODUCT.name());
                                          authConfig.requestMatchers(HttpMethod.POST, "/api/orders")
                                                      .hasAnyAuthority(Permission.SUBMIT_ONE_ORDER.name());
                                          authConfig.requestMatchers(HttpMethod.GET, "/api/orders", "/api/orders/*")
                                                      .hasAnyAuthority(Permission.READ_ORDERS.name());
                                          authConfig.anyRequest().denyAll();
                                    });

            return http.build();
      }

      @Bean
      AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
      }

      @Bean
      AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(userDetailsService);
            provider.setPasswordEncoder(passwordEncoder());
            return provider;
      }

      @Bean
      PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
      }

}

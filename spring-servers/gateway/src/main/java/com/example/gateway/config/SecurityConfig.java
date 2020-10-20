package com.example.gateway.config;

import com.example.gateway.security.AuthenticationManager;
import com.example.gateway.security.SecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthenticationManager authenticationManager;

  private final SecurityContextRepository securityContextRepository;

  @Bean
  SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {

    return http.cors().disable()
        .exceptionHandling()
        .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> swe.getResponse()
            .setStatusCode(HttpStatus.UNAUTHORIZED)))
        .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> swe.getResponse()
            .setStatusCode(HttpStatus.FORBIDDEN)))
        .and()
        .csrf().disable()
        .authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
        .pathMatchers("/auth/**", "/users/**")
        .permitAll()
        .and()
        .authorizeExchange()
        .pathMatchers("/ws/**")
        .authenticated()
        .and()
        .authorizeExchange()
        .pathMatchers("/actuator/**")
        .permitAll()
        .anyExchange().authenticated()
        .and()
        .build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


}


package com.example.gateway.controller;

import com.example.gateway.dto.ResponseWrapper;
import com.example.gateway.dto.UserCredentialDto;
import com.example.gateway.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

  private final AuthService authService;
  private final ReactiveRedisTemplate<String, String> redisTemplate;

  @Value("${redis.disconnect-event-topic}")
  private String DISCONNECT_EVENT_TOPIC;

  @Value("${redis.token-version-key}")
  private String TOKEN_VERSION_KEY;

  @PostMapping("login")
  public Mono<ResponseEntity<ResponseWrapper>> login(@RequestBody UserCredentialDto body) {
    return authService.login(body);
  }

  @PostMapping("logout")
  public Mono<ResponseEntity<ResponseWrapper>> logout(@AuthenticationPrincipal Principal principal, @RequestHeader(name = "Authorization") String token) {
    System.out.println(token);
    return redisTemplate.convertAndSend(DISCONNECT_EVENT_TOPIC, principal.getName())
        .flatMap(res -> redisTemplate.opsForHash().put(TOKEN_VERSION_KEY, principal.getName(),
            UUID.randomUUID().toString())
        )
        .flatMap(r -> Mono.just(ResponseEntity.ok(ResponseWrapper.of("LOGGED_OUT", ""))));
  }

}

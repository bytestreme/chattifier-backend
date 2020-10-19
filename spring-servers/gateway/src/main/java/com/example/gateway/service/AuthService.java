package com.example.gateway.service;

import com.example.gateway.dto.ResponseWrapper;
import com.example.gateway.dto.UserCredentialDto;
import com.example.gateway.model.user.User;
import com.example.gateway.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final BCryptPasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final UserRepository userRepository;
  private final ReactiveRedisTemplate<String, String> redisTemplate;

  @Value("${redis.token-version-key}")
  private String TOKEN_VERSION_KEY;

  @NonNull
  public Mono<ResponseEntity<ResponseWrapper>> register(UserCredentialDto user) {
    return userRepository.findByUsername(user.getUsername())
        .map(u -> ResponseEntity.badRequest()
            .body(ResponseWrapper.of("USER_EXISTS", Strings.EMPTY)))
        .switchIfEmpty(
            userRepository.save(User.builder()
                .id(null)
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(List.of("ROLE_USER"))
                .build()
            )
                .map(u -> ResponseEntity.ok()
                    .body(ResponseWrapper.of("USER_CREATED", user.getUsername())))
        );
  }

  @NonNull
  public Mono<ResponseEntity<ResponseWrapper>> login(UserCredentialDto body) {
    String version = UUID.randomUUID().toString();
    return userRepository.findByUsername(body.getUsername())
        .filter(u -> passwordEncoder.matches(body.getPassword(), u.getPassword()))
        .map(z -> tokenService.generateToken(z, version))
        .doOnNext(z -> userRepository.findByUsername(body.getUsername())
            .flatMap(us -> redisTemplate.opsForHash().put(TOKEN_VERSION_KEY, us.getId(), version))
            .subscribe()
        )
        .map(u -> ResponseEntity.ok()
            .body(ResponseWrapper.of("LOGIN_SUCCESS", u)))
        .switchIfEmpty(Mono.just(ResponseEntity.badRequest()
            .body(ResponseWrapper.of("INVALID_CREDENTIALS", Strings.EMPTY))));
  }

}

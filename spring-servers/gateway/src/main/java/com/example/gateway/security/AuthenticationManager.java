package com.example.gateway.security;

import com.example.gateway.service.TokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

  private final TokenService tokenService;
  private final ReactiveRedisTemplate<String, String> redisTemplate;

  @Value("${jwt.roles-claim}")
  private String ROLES_CLAIM;

  @Value("${jwt.token-version-claim}")
  private String VERSION_CLAIM;

  @Value("${redis.token-version-key}")
  private String TOKEN_VERSION_KEY;

  @Override
  @SuppressWarnings("unchecked")
  public Mono<Authentication> authenticate(Authentication authentication) {
    String authToken = authentication.getCredentials().toString();
    String username;
    try {
      username = tokenService.getUsernameFromToken(authToken);
    } catch (Exception e) {
      username = null;
    }
    if (username != null) {
      Claims claims = tokenService.getAllClaimsFromToken(authToken);
      List<String> roles = (List<String>) claims.get(ROLES_CLAIM, List.class);
      String tokenVersion = claims.get(VERSION_CLAIM, String.class);

      List<SimpleGrantedAuthority> authorities = roles.stream()
          .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username, authorities);
      SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(username, authorities));

      return redisTemplate.opsForHash().get(TOKEN_VERSION_KEY, username)
          .filter(x -> ((String) x).equals(tokenVersion))
          .flatMap(y -> Mono.<Authentication>just(auth))
          .switchIfEmpty(Mono.error(new AccountExpiredException("TOKEN_INVALID")));

    } else {
      return Mono.error(new AccountExpiredException("TOKEN_INVALID"));
    }
  }
}

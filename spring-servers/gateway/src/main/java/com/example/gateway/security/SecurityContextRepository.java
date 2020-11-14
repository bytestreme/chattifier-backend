package com.example.gateway.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.AbstractServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

  @Value("${jwt.ws-auth-prefix}")
  private String WS_AUTH_PREFIX;

  @Value("${jwt.http-auth-header-prefix}")
  private String TOKEN_PREFIX;

  private final AuthenticationManager authenticationManager;

  @Override
  public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange swe) {
    ServerHttpRequest request = swe.getRequest();
    String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    String wsAuth = request.getQueryParams().getFirst(WS_AUTH_PREFIX);
    String authToken = null;
    if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
      authToken = authHeader.replace(TOKEN_PREFIX, "");
    }
    if (wsAuth != null && !wsAuth.isBlank()) {
      authToken = wsAuth;
    }

    if (authToken != null) {
      Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
      return this.authenticationManager.authenticate(auth)
          .onErrorMap(
              er -> er instanceof BadCredentialsException,
              autEx -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)
          )
          .map(SecurityContextImpl::new);
    } else {
      return Mono.empty();
    }
  }

}

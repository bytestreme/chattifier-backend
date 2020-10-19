package com.example.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

  public CustomFilter() {
    super(Config.class);
  }

  @Value("${headers.gateway.user-id-forwarded}")
  private String USER_ID_HEADER;

  @Value("${headers.gateway.user-roles-forwarded}")
  private String USER_ROLES_HEADER;

  @Value("${headers.gateway.user-roles-delimiter}")
  private String USER_ROLES_DELIMITER;


  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> exchange.getPrincipal()
        .flatMap(p -> {
          var req = exchange.getRequest().mutate()
              .header(USER_ID_HEADER, (String) ((Authentication) p).getPrincipal())
              .header(USER_ROLES_HEADER
                  , ((Authentication) p).getAuthorities()
                      .stream()
                      .map(GrantedAuthority::getAuthority)
                      .collect(Collectors.joining(USER_ROLES_DELIMITER))
              )
              .build();
          return chain.filter(exchange.mutate().request(req).build());
        });
  }

  public static class Config {

  }
}
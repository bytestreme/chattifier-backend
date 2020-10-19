package com.example.messagedistributeservice.config;


import com.example.messagedistributeservice.channel.MessageDistributeChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfiguration {

  @Value("${ws.route.get-messages}")
  private String GET_MESSAGES_ROUTE;

  @Bean
  public HandlerAdapter webSocketHandlerAdapter() {
    return new WebSocketHandlerAdapter();
  }

  @Bean
  public HandlerMapping webSocketHandler(MessageDistributeChannel messageDistributeChannel) {
    SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
    Map<String, WebSocketHandler> urlMap = new HashMap<>();
    urlMap.put(GET_MESSAGES_ROUTE, messageDistributeChannel);
    mapping.setUrlMap(urlMap);
    mapping.setOrder(0);
    return mapping;
  }

}
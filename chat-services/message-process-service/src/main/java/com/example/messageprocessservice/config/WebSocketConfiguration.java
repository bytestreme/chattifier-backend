package com.example.messageprocessservice.config;

import com.example.messageprocessservice.channel.MessageReceiver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfiguration {

  @Value("${ws.route.send-message-route}")
  private String ROUTE_SEND_MESSAGE;

  @Bean
  public HandlerAdapter webSocketHandlerAdapter() {
    return new WebSocketHandlerAdapter(
        new HandshakeWebSocketService(
            new ReactorNettyRequestUpgradeStrategy()
        )
    );
  }

  @Bean
  public HandlerMapping webSocketHandler(MessageReceiver messageReceiver) {
    SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
    Map<String, WebSocketHandler> urlMap = new HashMap<>();
    urlMap.put(ROUTE_SEND_MESSAGE, messageReceiver);
    mapping.setUrlMap(urlMap);
    mapping.setOrder(0);
    return mapping;
  }


}
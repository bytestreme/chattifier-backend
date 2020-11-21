package com.example.messagedeliveryservice.channel;

import com.example.messagedeliveryservice.service.MessageUnicastService;
import com.example.messagedeliveryservice.ws.WebSocketMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageDeliveryChannel implements WebSocketHandler {

  @Value("${headers.gateway.user-id-forwarded}")
  private String USER_ID_HEADER;

  @Value("${redis.disconnect-event-topic}")
  private String DISCONNECT_EVENT_TOPIC;

  private final WebSocketMessageMapper mapper;
  private final ReactiveRedisMessageListenerContainer listenerContainer;
  private final MessageUnicastService unicastService;

  @Override
  @NonNull
  public Mono<Void> handle(WebSocketSession session) {
    String chatId = session.getHandshakeInfo().getHeaders().getFirst(USER_ID_HEADER);
    return session.receive()
        .map(WebSocketMessage::retain)
        .map(WebSocketMessage::getPayload)
        .publishOn(Schedulers.elastic())
        .transform(ax -> unicastService.getMessages(chatId)
            .transform(x -> mapper.encode(x, session.bufferFactory()))
            .map(db -> new WebSocketMessage(WebSocketMessage.Type.TEXT, db))
        )
        .onBackpressureBuffer()
        .takeUntilOther(listenerContainer.receive(ChannelTopic.of(DISCONNECT_EVENT_TOPIC))
            .handle((disconnectEvent, sink) -> {
              if (disconnectEvent.getMessage().equals(chatId)) {
                sink.next(disconnectEvent);
              }
            })
        )
        .as(session::send);

  }

}

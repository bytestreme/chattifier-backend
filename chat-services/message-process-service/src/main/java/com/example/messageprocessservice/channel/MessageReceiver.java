package com.example.messageprocessservice.channel;

import com.example.messageprocessservice.domain.kafka.MessageInput;
import com.example.messageprocessservice.ws.WebSocketMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageReceiver implements WebSocketHandler {

  @Qualifier("messageProducer")
  private final KafkaSender<String, MessageInput> messageProducer;
  private final WebSocketMessageMapper mapper;
  private final ReactiveRedisMessageListenerContainer listenerContainer;

  @Value("${kafka.messagesInput.topic}")
  private String TOPIC_NAME;

  @Value("${headers.gateway.user-id-forwarded}")
  private String USER_ID_HEADER;

  @Value("${service.message-delivered-key}")
  private String CODE_MESSAGE_DELIVERED;

  @Value("${service.message-not-delivered-key}")
  private String CODE_MESSAGE_FAILED;

  @Value("${redis.disconnect-event-topic}")
  private String DISCONNECT_EVENT_TOPIC;


  @Override
  public Mono<Void> handle(WebSocketSession session) {
    String chatId = session.getHandshakeInfo().getHeaders().getFirst(USER_ID_HEADER);
    return session.receive()
        .map(WebSocketMessage::retain)
        .map(WebSocketMessage::getPayload)
        .publishOn(Schedulers.elastic())
        .transform(mapper::decode)
        .transform(mi -> this.doHandle(mi, chatId))
        .onBackpressureBuffer()
        .transform(m -> mapper.encode(m, session.bufferFactory()))
        .map(db -> new WebSocketMessage(WebSocketMessage.Type.TEXT, db))
        .as(session::send)
        .takeUntilOther(listenerContainer.receive(ChannelTopic.of(DISCONNECT_EVENT_TOPIC))
            .handle((de, sink) -> {
              if (de.getMessage().equals(chatId)) {
                sink.next(de);
              }
            })
        );
  }


  private Flux<?> doHandle(Flux<MessageInput> inbound, String username) {
    return inbound
        .flatMap(x ->
            messageProducer
                .send(Mono.just(messageToSenderRecord(x, username)))
                .next()
                .map(stringSenderResult -> stringSenderResult.exception() == null)
        )
        .map(aBoolean -> aBoolean ? CODE_MESSAGE_DELIVERED : CODE_MESSAGE_FAILED);
  }

  private SenderRecord<String, MessageInput, String>
  messageToSenderRecord(MessageInput messageInput, String username) {
    messageInput.setSender(username);
    return SenderRecord.create(new ProducerRecord<>(TOPIC_NAME, "chatMessages", messageInput), messageInput.getId());
  }

}

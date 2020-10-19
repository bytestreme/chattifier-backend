package com.example.messagedistributeservice.service;

import com.example.messagedistributeservice.domain.kafka.MessageInput;
import com.example.messagedistributeservice.domain.repository.message.MessageDocument;
import com.example.messagedistributeservice.domain.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class KafkaService {

  @Qualifier("messageConsumer")
  private final KafkaReceiver<String, MessageInput> messageConsumer;
  private final MessageUnicastService messageUnicastService;
  private final MessageRepository messageRepository;

  @PostConstruct
  public void init() {
    messageConsumer
        .receive()
        .doOnNext(r -> r.receiverOffset().acknowledge())
        .map(this::toMessageInput)
        .doOnNext(messageUnicastService::onNext)
        .flatMap(this::persistMessages)
        .publish()
        .connect();
  }

  private MessageInput toMessageInput(ReceiverRecord<String, MessageInput> consumerRecord) {
    return new MessageInput(consumerRecord.value().getText(),
        consumerRecord.value().getChatId(), consumerRecord.value().getSender()
    );
  }

  private Mono<MessageDocument> persistMessages(MessageInput messageInput) {
    return messageRepository.save(
        MessageDocument.builder()
            .id(messageInput.getId())
            .text(messageInput.getText())
            .sender(messageInput.getSender())
            .chatId(messageInput.getChatId())
            .timestamp(messageInput.getTimestamp())
            .build()
    );
  }

}

package com.example.messagedistributeservice.service;

import com.example.messagedistributeservice.domain.kafka.MessageInput;
import com.example.messagedistributeservice.domain.repository.message.MessageDocument;
import com.example.messagedistributeservice.domain.repository.message.MessageRepository;
import com.example.messagedistributeservice.domain.repository.timestamp.UserMessageTimestamp;
import com.example.messagedistributeservice.domain.repository.timestamp.UserMessageTimestampRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageUnicastService {

  private final MessageRepository messageRepository;
  private final UserMessageTimestampRepository timestampRepository;
  private final EmitterProcessor<MessageInput> processor = EmitterProcessor.create();

  public void onNext(MessageInput next) {
    processor.onNext(next);
  }

  public Flux<MessageInput> getMessages(String chatId) {
    Flux<MessageInput> unreadMessagesFlux =
        Flux.from(timestampRepository.findById(chatId)
            .defaultIfEmpty(getDefault(chatId)))
            .flatMap(ts -> filterByTimestamp(chatId, ts))
            .map(this::fromDocument);

    Flux<MessageInput> realTimeMessagesFlux = processor.publish()
        .autoConnect()
        .filter(mi -> mi.getChatId().equals(chatId));

    return Flux.concat(unreadMessagesFlux, realTimeMessagesFlux)
        .doOnNext(
            md -> timestampRepository.save(
                withChatIdAndTimestamp(chatId, md.getTimestamp())
            ).subscribe()
        );

  }

  private MessageInput fromDocument(MessageDocument messageDocument) {
    return MessageInput.builder()
        .id(messageDocument.getId())
        .chatId(messageDocument.getChatId())
        .sender(messageDocument.getSender())
        .text(messageDocument.getText())
        .timestamp(messageDocument.getTimestamp())
        .build();
  }

  private Flux<MessageDocument> filterByTimestamp(String chatId, UserMessageTimestamp timestamp) {
    return messageRepository.findAllByChatIdAndTimestampGreaterThan(chatId, timestamp.getTimestamp());
  }

  private UserMessageTimestamp getDefault(String chatId) {
    return UserMessageTimestamp
        .builder()
        .timestamp(0L)
        .userId(chatId)
        .build();
  }

  private UserMessageTimestamp withChatIdAndTimestamp(String chatId, Long timestamp) {
    return UserMessageTimestamp.builder()
        .timestamp(timestamp)
        .userId(chatId)
        .build();
  }


}

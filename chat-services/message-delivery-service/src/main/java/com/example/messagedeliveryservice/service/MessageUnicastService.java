package com.example.messagedeliveryservice.service;

import com.example.messagedeliveryservice.model.kafka.MessageInput;
import com.example.messagedeliveryservice.model.repository.message.MessageRepository;
import com.example.messagedeliveryservice.model.repository.message.MessageTable;
import com.example.messagedeliveryservice.model.repository.timestamp.UserMessageTimestamp;
import com.example.messagedeliveryservice.model.repository.timestamp.UserMessageTimestampRepository;
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

    private MessageInput fromDocument(MessageTable messageTable) {
        return MessageInput.create(
                messageTable.getKey().getId().toString(),
                messageTable.getKey().getTimestamp(),
                messageTable.getText(),
                messageTable.getKey().getChatId(),
                messageTable.getSender()
        );
    }

    private Flux<MessageTable> filterByTimestamp(String chatId, UserMessageTimestamp timestamp) {
        return messageRepository.findAllByKeyChatIdAndKeyTimestampGreaterThan(chatId, timestamp.getTimestamp());
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

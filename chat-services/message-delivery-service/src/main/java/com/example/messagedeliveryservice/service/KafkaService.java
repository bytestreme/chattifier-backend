package com.example.messagedeliveryservice.service;

import com.example.messagedeliveryservice.model.kafka.MessageInput;
import com.example.messagedeliveryservice.model.repository.message.MessageKey;
import com.example.messagedeliveryservice.model.repository.message.MessageRepository;
import com.example.messagedeliveryservice.model.repository.message.MessageTable;
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
        return MessageInput.create(
                consumerRecord.value().getText(),
                consumerRecord.value().getChatId(),
                consumerRecord.value().getSender()
        );
    }

    private Mono<MessageTable> persistMessages(MessageInput messageInput) {
        MessageKey key = MessageKey.create(
                messageInput.getTimestamp(),
                messageInput.getChatId()
        );
        return messageRepository.save(
                MessageTable.create(key,
                        messageInput.getText(),
                        messageInput.getSender()
                )
        );
    }

}

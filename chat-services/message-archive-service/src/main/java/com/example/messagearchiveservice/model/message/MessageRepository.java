package com.example.messagearchiveservice.model.message;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MessageRepository extends ReactiveCassandraRepository<MessageTable, MessageKey> {

    Flux<MessageTable> findAllByKeyChatIdAndKeyTimestampLessThanEqual(String chatId, Long timestamp);
    Flux<MessageTable> findAllByKeyChatId(String chatId);

}

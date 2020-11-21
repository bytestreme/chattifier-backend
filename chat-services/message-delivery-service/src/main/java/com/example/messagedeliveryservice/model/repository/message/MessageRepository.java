package com.example.messagedeliveryservice.model.repository.message;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MessageRepository extends ReactiveCassandraRepository<MessageTable, MessageKey> {

    Flux<MessageTable> findAllByKeyChatIdAndKeyTimestampGreaterThan(String chatId, Long timestamp);

}

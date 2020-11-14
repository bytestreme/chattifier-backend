package com.example.messagearchiveservice.domain.message;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MessageRepository extends ReactiveMongoRepository<MessageDocument, String> {

  Flux<MessageDocument> findAllByChatId(String chatId);

  Flux<MessageDocument> findAllByChatIdAndTimestampGreaterThan(String chatId, Long timestamp);

  Flux<MessageDocument> findAllByChatIdAndTimestampLessThanEqual(String chatId, Long timestamp);

}

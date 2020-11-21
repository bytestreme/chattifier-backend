package com.example.messagearchiveservice.model.timestamp;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserMessageTimestampRepository extends ReactiveMongoRepository<UserMessageTimestamp, String> {

  Flux<UserMessageTimestamp> findByUserId(String userId);

}

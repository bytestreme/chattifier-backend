package com.example.messageprocessservice.domain.repository.message;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends ReactiveMongoRepository<MessageDocument, String> {

}

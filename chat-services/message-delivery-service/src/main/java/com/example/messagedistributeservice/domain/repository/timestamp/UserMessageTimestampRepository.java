package com.example.messagedistributeservice.domain.repository.timestamp;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMessageTimestampRepository extends ReactiveMongoRepository<UserMessageTimestamp, String> {
}

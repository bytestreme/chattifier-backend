package com.example.userssearchservice.domain;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

  Flux<User> findAllByUsernameStartingWith(String username);

}

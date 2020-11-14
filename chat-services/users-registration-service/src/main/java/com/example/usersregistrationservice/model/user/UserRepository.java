package com.example.usersregistrationservice.model.user;


import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCassandraRepository<User, UserKey> {

  Mono<User> findByKeyUsername(String username);
}

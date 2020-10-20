package com.example.userssearchservice.service;

import com.example.userssearchservice.domain.User;
import com.example.userssearchservice.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class UsersLookupService {

  private final UserRepository userRepository;

  public Flux<User> getAllUsers() {
    return userRepository.findAll();
  }

}

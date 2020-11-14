package com.example.usersregistrationservice.service;

import com.example.usersregistrationservice.dto.ResponseWrapper;
import com.example.usersregistrationservice.dto.UserCredentialDto;
import com.example.usersregistrationservice.model.user.User;
import com.example.usersregistrationservice.model.user.UserKey;
import com.example.usersregistrationservice.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  @NonNull
  public Mono<ResponseEntity<ResponseWrapper>> register(UserCredentialDto user) {
    return userRepository.findByKeyUsername(user.getUsername())
        .map(u -> ResponseEntity.badRequest()
            .body(ResponseWrapper.of("USER_EXISTS", Strings.EMPTY)))
        .switchIfEmpty(
            userRepository.save(User.builder()
                .key(new UserKey(UUID.randomUUID(), user.getUsername()))
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(List.of("ROLE_USER"))
                .build()
            )
                .map(u -> ResponseEntity.ok()
                    .body(ResponseWrapper.of("USER_CREATED", user.getUsername())))
        );
  }
}

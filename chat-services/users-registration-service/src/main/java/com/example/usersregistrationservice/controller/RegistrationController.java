package com.example.usersregistrationservice.controller;

import com.example.usersregistrationservice.dto.ResponseWrapper;
import com.example.usersregistrationservice.dto.UserCredentialDto;
import com.example.usersregistrationservice.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

  private final RegistrationService registrationService;

  @PostMapping("register")
  public Mono<ResponseEntity<ResponseWrapper>> register(@RequestBody UserCredentialDto body) {
    return registrationService.register(body);
  }

}

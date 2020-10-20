package com.example.userssearchservice.controller;

import com.example.userssearchservice.domain.User;
import com.example.userssearchservice.service.UsersLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UsersSearchController {

  private final UsersLookupService lookupService;

  @GetMapping
  public Flux<User> allUsers() {
    return lookupService.getAllUsers();
  }

}

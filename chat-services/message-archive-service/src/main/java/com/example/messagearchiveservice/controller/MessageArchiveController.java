package com.example.messagearchiveservice.controller;

import com.example.messagearchiveservice.domain.message.MessageDocument;
import com.example.messagearchiveservice.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("messages")
public class MessageArchiveController {

  private final ArchiveService archiveService;

  @GetMapping("all")
  public Flux<MessageDocument> getAllMessages(@RequestHeader("X-G-User") String userId) {
    return archiveService.getAllMessages(userId);
  }

  @GetMapping("allRead")
  public Flux<MessageDocument> getAllReadMessages(@RequestHeader("X-G-User") String userId) {
    return archiveService.getAllReadMessages(userId);
  }

}

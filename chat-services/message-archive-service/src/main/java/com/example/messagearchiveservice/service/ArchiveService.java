package com.example.messagearchiveservice.service;

import com.example.messagearchiveservice.domain.message.MessageDocument;
import com.example.messagearchiveservice.domain.message.MessageRepository;
import com.example.messagearchiveservice.domain.timestamp.UserMessageTimestampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ArchiveService {

  private final MessageRepository messageRepository;
  private final UserMessageTimestampRepository timestampRepository;


  public Flux<MessageDocument> getAllMessages(String userId) {
    return messageRepository.findAllByChatId(userId);
  }

  public Flux<MessageDocument> getAllReadMessages(String userId) {
    return timestampRepository.findByUserId(userId)
        .flatMap(ts -> messageRepository
            .findAllByChatIdAndTimestampLessThanEqual(userId, ts.getTimestamp()));
  }


}

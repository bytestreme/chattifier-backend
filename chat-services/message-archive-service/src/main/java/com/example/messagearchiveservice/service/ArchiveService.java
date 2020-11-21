package com.example.messagearchiveservice.service;

import com.example.messagearchiveservice.model.message.MessageRepository;
import com.example.messagearchiveservice.model.message.MessageTable;
import com.example.messagearchiveservice.model.timestamp.UserMessageTimestampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ArchiveService {

    private final MessageRepository messageRepository;
    private final UserMessageTimestampRepository timestampRepository;


    public Flux<MessageTable> getAllMessages(String userId) {
        return messageRepository.findAllByKeyChatId(userId);
    }

    public Flux<MessageTable> getAllReadMessages(String userId) {
        return timestampRepository.findByUserId(userId)
                .flatMap(ts -> messageRepository
                        .findAllByKeyChatIdAndKeyTimestampLessThanEqual(userId, ts.getTimestamp()));
    }


}

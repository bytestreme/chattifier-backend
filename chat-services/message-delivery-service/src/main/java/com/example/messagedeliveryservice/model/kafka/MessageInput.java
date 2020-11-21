package com.example.messagedeliveryservice.model.kafka;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class MessageInput {

    private String id = UUID.randomUUID().toString();

    @NonNull
    private Long timestamp = Instant.now().getEpochSecond();

    @NonNull
    private String text;

    @NonNull
    private String chatId;

    @NonNull
    private String sender;

    public static MessageInput create(String id, Long timestamp, String text, String chatId, String sender) {
        return new MessageInput(id, timestamp, text, chatId, sender);
    }

    public static MessageInput create(String text, String chatId, String sender) {
        return new MessageInput(text, chatId, sender);
    }
}

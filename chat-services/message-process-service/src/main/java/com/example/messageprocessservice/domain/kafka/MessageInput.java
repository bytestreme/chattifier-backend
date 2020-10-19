package com.example.messageprocessservice.domain.kafka;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class MessageInput {

  private String id = UUID.randomUUID().toString();

  private Long timestamp = Instant.now().getEpochSecond();

  @NonNull
  private String text;

  @NonNull
  private String chatId;

  @NonNull
  private String sender;

}

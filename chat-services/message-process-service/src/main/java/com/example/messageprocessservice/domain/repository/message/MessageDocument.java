package com.example.messageprocessservice.domain.repository.message;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messages")
public class MessageDocument {

  @Id
  private String id;

  @NonNull
  private Long timestamp;

  @NonNull
  private String text;

  @NonNull
  private String chatId;

  @NonNull
  private String sender;

}

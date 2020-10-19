package com.example.messagedistributeservice.domain.repository.message;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messages")
public class MessageDocument {

  @Id
  private String id;

  @NonNull
  @Field(name = "timestamp")
  private Long timestamp;

  @NonNull
  @Field(name = "text")
  private String text;

  @NonNull
  @Field(name = "chatId")
  @Indexed
  private String chatId;

  @NonNull
  @Field(name = "sender")
  private String sender;

}

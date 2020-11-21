package com.example.messagearchiveservice.model.timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "message_timestamp")
public class UserMessageTimestamp {

  @Id
  private String userId;

  private Long timestamp = 0L;

}

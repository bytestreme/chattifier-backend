package com.example.messagedistributeservice.domain.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class MessageInputDeserializer implements Deserializer<MessageInput> {

  @Override
  public MessageInput deserialize(String topic, byte[] data) {
    ObjectMapper mapper = new ObjectMapper();
    MessageInput messageInput = null;
    try {
      messageInput = mapper.readValue(data, MessageInput.class);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return messageInput;
  }

}

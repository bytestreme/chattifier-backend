package com.example.messagedeliveryservice.model.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class MessageInputSerializer implements Serializer<MessageInput> {

  @Override
  public byte[] serialize(String topic, MessageInput data) {
    byte[] retVal = null;
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      retVal = objectMapper.writeValueAsString(data).getBytes();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return retVal;
  }

}

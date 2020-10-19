package com.example.messageprocessservice.config;

import com.example.messageprocessservice.domain.kafka.MessageInput;
import com.example.messageprocessservice.domain.kafka.MessageInputSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

  @Value("${kafka.bootstrap.servers}")
  private String BOOTSTRAP_SERVERS;

  @Bean("messageProducer")
  public KafkaSender<String, MessageInput> messageProducer() {
    Map<String, Object> config = new HashMap<>();

    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageInputSerializer.class);

    SenderOptions<String, MessageInput> senderOptions =
        SenderOptions.<String, MessageInput>create(config)
            .maxInFlight(1024);
    return KafkaSender.create(senderOptions);
  }

}
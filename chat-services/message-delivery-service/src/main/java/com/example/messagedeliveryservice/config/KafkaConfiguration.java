package com.example.messagedeliveryservice.config;

import com.example.messagedeliveryservice.model.kafka.MessageInput;
import com.example.messagedeliveryservice.model.kafka.MessageInputDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

  @Value("${kafka.bootstrap.servers}")
  private String BOOTSTRAP_SERVERS;

  @Value("${kafka.messagesInput.topic}")
  private String TOPIC_NAME;

  @Value("${kafka.consumer-group}")
  private String CONSUMER_GROUP;

  @Value("${kafka.auto-reset-config}")
  private String AUTO_RESET_CONFIG;

  @Bean("messageConsumer")
  public KafkaReceiver<String, MessageInput> messageConsumer() {
    Map<String, Object> consumerProps = new HashMap<>();

    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP);
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageInputDeserializer.class);
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_RESET_CONFIG);
    consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

    ReceiverOptions<String, MessageInput> receiverOptions = ReceiverOptions.
        <String, MessageInput>create(consumerProps)
        .subscription(Collections.singleton(TOPIC_NAME));

    return KafkaReceiver.create(receiverOptions);
  }


}
package com.example.messageprocessservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;

@Configuration
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String HOST;

  @Value("${spring.redis.port}")
  private Integer PORT;

  @Bean
  public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
    return new LettuceConnectionFactory(HOST, PORT);
  }

  @Bean
  public ReactiveRedisMessageListenerContainer redisContainer() {
    return new ReactiveRedisMessageListenerContainer(reactiveRedisConnectionFactory());
  }

}

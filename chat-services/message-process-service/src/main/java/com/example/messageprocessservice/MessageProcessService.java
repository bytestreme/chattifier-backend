package com.example.messageprocessservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MessageProcessService {

    public static void main(String[] args) {
        SpringApplication.run(MessageProcessService.class, args);
    }

}

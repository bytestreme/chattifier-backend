package com.example.messagearchiveservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

@SpringBootApplication
@EnableEurekaClient
public class MessageArchiveService {

    public static void main(String[] args) {
        SpringApplication.run(MessageArchiveService.class, args);
    }

}

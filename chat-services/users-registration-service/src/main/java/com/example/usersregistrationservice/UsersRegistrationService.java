package com.example.usersregistrationservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UsersRegistrationService {

  public static void main(String[] args) {
    SpringApplication.run(UsersRegistrationService.class, args);
  }

}
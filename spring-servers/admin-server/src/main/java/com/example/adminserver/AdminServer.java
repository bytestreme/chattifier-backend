package com.example.adminserver;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableAdminServer
public class AdminServer {

  public static void main(String[] args) {
    SpringApplication.run(AdminServer.class, args);
  }

}
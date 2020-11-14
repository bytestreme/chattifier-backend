#!/bin/bash

echo './gradlew clean build -x test --console plain'
./gradlew clean build -x test --console plain

echo './gradlew :spring-servers:config-server:docker --console plain'
./gradlew :spring-servers:config-server:docker --console plain

echo './gradlew :spring-servers:eureka-server:docker --console plain'
./gradlew :spring-servers:eureka-server:docker --console plain

echo './gradlew :spring-servers:admin-server:docker --console plain'
./gradlew :spring-servers:admin-server:docker --console plain

echo './gradlew :spring-servers:gateway:docker --console plain'
./gradlew :spring-servers:gateway:docker --console plain

echo './gradlew :chat-services:message-process-service:docker --console plain'
./gradlew :chat-services:message-process-service:docker --console plain

echo './gradlew :chat-services:message-delivery-service:docker --console plain'
./gradlew :chat-services:message-delivery-service:docker --console plain

echo './gradlew :chat-services:users-search-service:docker --console plain'
./gradlew :chat-services:users-search-service:docker --console plain

echo './gradlew :chat-services:users-registration-service:docker --console plain'
./gradlew :chat-services:users-registration-service:docker --console plain

echo './gradlew :chat-services:message-archive-service:docker --console plain'
./gradlew :chat-services:message-archive-service:docker --console plain

echo 'Docker images created'
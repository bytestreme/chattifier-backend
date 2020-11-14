#!/bin/bash

echo './gradlew :spring-servers:config-server:dockerPush --console plain'
./gradlew :spring-servers:config-server:dockerPush --console plain

echo './gradlew :spring-servers:eureka-server:dockerPush --console plain'
./gradlew :spring-servers:eureka-server:dockerPush --console plain

echo './gradlew :spring-servers:admin-server:dockerPush --console plain'
./gradlew :spring-servers:admin-server:dockerPush --console plain

echo './gradlew :spring-servers:gateway:dockerPush --console plain'
./gradlew :spring-servers:gateway:dockerPush --console plain

echo './gradlew :chat-services:message-process-service:dockerPush --console plain'
./gradlew :chat-services:message-process-service:dockerPush --console plain

echo './gradlew :chat-services:message-delivery-service:dockerPush --console plain'
./gradlew :chat-services:message-delivery-service:dockerPush --console plain

echo './gradlew :chat-services:users-search-service:dockerPush --console plain'
./gradlew :chat-services:users-search-service:dockerPush --console plain

echo './gradlew :chat-services:users-registration-service:dockerPush --console plain'
./gradlew :chat-services:users-registration-service:dockerPush --console plain

echo './gradlew :chat-services:message-archive-service:dockerPush --console plain'
./gradlew :chat-services:message-archive-service:dockerPush --console plain

echo 'Docker images pushed'
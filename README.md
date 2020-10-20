<h3>Chattifier Backend</h5>
<img src="https://travis-ci.com/bytestreme/chattifier-backend.svg?token=spRRcQKowAe4Cq6N3trv&branch=master">
<br />
<br />

Chattifier Backend - is a scalable Spring Cloud-based project for real-time message exchange

It consists of the following components

* Spring Cloud Servers
  - Config Server - loads configuration of each service, so that every microservice can load its own config upon deployment and at runtime
  - Eureka Service Locator - running services register themselves into the registry
  - Admin Server - interacts with the discovered services' Actuator to monitor them
  - Gateway - secured API entry point which authenticated, proxies requests and forwards credentials to the services 
 
* Chat Services
  - Message Processing Service - handle user-sent messages
  - Message Delivery Service - distribute the messages destined to the client
  

<img src="/img/scheme.png" />

Images could be built using `docker-create-images.sh` to create and `docker-push-images.sh` to push them.


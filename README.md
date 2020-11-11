<h3>Chattifier Backend</h3>
<img src="https://travis-ci.com/bytestreme/chattifier-backend.svg?token=spRRcQKowAe4Cq6N3trv&branch=master">


Chattifier Backend - is a scalable Spring Cloud project for real-time message exchange

It consists of the following components

* Spring Cloud Servers
  - Config Server - loads configuration of each service, so that every microservice can load its own config upon deployment and at runtime
  - Eureka Service Locator - running services register themselves into the registry
  - Admin Server - interacts with the discovered services' Actuator to monitor them
  - Gateway - secured API entry point which authenticates and authorizes users, proxies requests and forwards credentials to the services 
 
* Chat Services
  - Message Processing Service - handle user-sent messages
  - Message Delivery Service - distribute the messages destined to the client
  - User Search Service - search for users to send messages

<img src="/img/scheme.png" />

Images could be built using `docker-create-images.sh` to create and `docker-push-images.sh` to push them.

<h3>TODO</h3>

- [ ] K8s cloud deploy
- [ ] Threads profiling 
- [ ] Stress Testing
- [ ] Load testing
- [ ] Autoscaling testing
- [ ] Additional chat services


## High Level Design
![image](/resources/architechture.svg)

### Tech Stack
Spring boot microservices with Java, MySQL, MongoDB, Redis cache, Kafka as Message Queue and Web Sockets for sending notifications to clients.

### Project Setup
Set `server.port` in `application-local.properties` for all local services. 
To ensure we can run them in parallel locally, but they are all served from 8080 when deployed.  

Similarly, setup other properties required for the same from secret manager.
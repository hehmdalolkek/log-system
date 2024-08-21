# Log System

Spring-Boot projects.

### Log-Collector

REST-API. Receives log in JSON format and sends to the Kafka topic "logs".

### Log Handler

Kafka Streams. Receives log from the "logs" topic. Filters logs with the ERROR type and sends them to the "errors" topic. 
Displays a message in the console when the error limit from one service instance is exceeded.

### Log Integrator

The service listens to the errors topic and saves errors in the database.

### Admin Server

A server based on de.codecentric:spring-boot-admin-starter-server. Shows the health of all services.

## Technologies

* Java 21
* Maven
* Spring Boot 3
* Spring Web
* Spring JDBC
* Spring Actuator
* Spring Admin
* Lombok
* H2
* Apache Kafka
* Kafka Streams

## Run locally

1. To run the application, you need to run Apache Kafka on http://localhost:9092.
2. Next, you can build and run the project from the command line:

```
cd log-collector
./mvnw package
java -jar .\target\log-collector-0.0.1-SNAPSHOT.jar

cd log-handler
./mvnw package
java -jar .\target\log-handler-0.0.1-SNAPSHOT.jar

cd log-integrator
./mvnw package
java -jar .\target\log-integrator-0.0.1-SNAPSHOT.jar

cd admin-server
./mvnw package
java -jar .\target\admin-server-0.0.1-SNAPSHOT.jar
```

3. You can send post log requests to http://localhost:8080/.
